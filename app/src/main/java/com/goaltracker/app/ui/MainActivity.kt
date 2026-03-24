package com.goaltracker.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.goaltracker.app.data.model.LegacyListItem
import com.goaltracker.app.data.model.MilestoneEntity
import com.goaltracker.app.data.model.ProjectEntity

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<GoalTrackerViewModel> {
        GoalTrackerViewModel.factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                GoalTrackerApp(viewModel = viewModel)
            }
        }
    }
}

private object Routes {
    const val Home = "home"
    const val Projects = "projects"
    const val Legacy = "legacy"
    const val CreateProject = "project/create"
    const val EditProject = "project/edit/{projectId}"
    const val ProjectDetail = "project/detail/{projectId}"
    const val CompleteProject = "project/complete/{projectId}"

    fun editProject(projectId: Long) = "project/edit/$projectId"
    fun projectDetail(projectId: Long) = "project/detail/$projectId"
    fun completeProject(projectId: Long) = "project/complete/$projectId"
}

@Composable
private fun GoalTrackerApp(viewModel: GoalTrackerViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                val topLevelItems = listOf(
                    Routes.Home to "홈",
                    Routes.Projects to "프로젝트",
                    Routes.Legacy to "레거시",
                )
                topLevelItems.forEach { (route, label) ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {},
                        label = { Text(label) },
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Routes.Projects) {
                FloatingActionButton(onClick = { navController.navigate(Routes.CreateProject) }) {
                    Text("추가")
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.Home) {
                val state by viewModel.homeState.collectAsStateWithLifecycle()
                HomeScreen(
                    state = state,
                    onProjectClick = { navController.navigate(Routes.projectDetail(it)) },
                    onProjectsClick = { navController.navigate(Routes.Projects) },
                )
            }
            composable(Routes.Projects) {
                val homeState by viewModel.homeState.collectAsStateWithLifecycle()
                val completedProjects by viewModel.completedProjects.collectAsStateWithLifecycle()
                ProjectsScreen(
                    activeProjects = homeState.activeProjects,
                    completedProjects = completedProjects,
                    onProjectClick = { navController.navigate(Routes.projectDetail(it)) },
                    onAddClick = { navController.navigate(Routes.CreateProject) },
                )
            }
            composable(Routes.Legacy) {
                val legacyItems by viewModel.legacyItems.collectAsStateWithLifecycle()
                LegacyScreen(items = legacyItems)
            }
            composable(Routes.CreateProject) {
                ProjectEditorScreen(
                    title = "프로젝트 만들기",
                    project = null,
                    onSave = { title, intent, progress ->
                        viewModel.createProject(title, intent, progress)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                )
            }
            composable(
                route = Routes.EditProject,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
                val detailState by viewModel.projectDetail(projectId).collectAsStateWithLifecycle()
                ProjectEditorScreen(
                    title = "프로젝트 수정",
                    project = detailState.project,
                    onSave = { title, intent, progress ->
                        detailState.project?.let { project ->
                            viewModel.updateProject(project, title, intent, progress)
                        }
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                )
            }
            composable(
                route = Routes.ProjectDetail,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
                val detailState by viewModel.projectDetail(projectId).collectAsStateWithLifecycle()
                val homeState by viewModel.homeState.collectAsStateWithLifecycle()
                ProjectDetailScreen(
                    project = detailState.project,
                    milestones = detailState.milestones,
                    supportMessage = homeState.supportMessage,
                    onSetFocus = { detailState.project?.let(viewModel::setFocusProject) },
                    onAddMilestone = { viewModel.addMilestone(projectId, it) },
                    onEdit = { navController.navigate(Routes.editProject(projectId)) },
                    onComplete = { navController.navigate(Routes.completeProject(projectId)) },
                )
            }
            composable(
                route = Routes.CompleteProject,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
                val detailState by viewModel.projectDetail(projectId).collectAsStateWithLifecycle()
                CompleteProjectScreen(
                    project = detailState.project,
                    onComplete = { achievement, lesson ->
                        detailState.project?.let { project ->
                            viewModel.completeProject(project, achievement, lesson)
                        }
                        navController.navigate(Routes.Legacy) {
                            popUpTo(Routes.Home)
                        }
                    },
                    onCancel = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    state: com.goaltracker.app.data.repo.HomeState,
    onProjectClick: (Long) -> Unit,
    onProjectsClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("골 트래커", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("포커스 프로젝트", fontWeight = FontWeight.Bold)
                    Text(state.focusProject?.title ?: "아직 포커스 프로젝트가 없다")
                    Text("진행률: ${state.focusProject?.progress ?: 0}%")
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("레거시 응원", fontWeight = FontWeight.Bold)
                    Text(state.supportMessage)
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("진행 중 프로젝트", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onProjectsClick) { Text("관리") }
            }
        }
        items(state.activeProjects.take(10), key = { it.id }) { project ->
            ProjectRow(project = project, onClick = { onProjectClick(project.id) })
        }
    }
}

@Composable
private fun ProjectsScreen(
    activeProjects: List<ProjectEntity>,
    completedProjects: List<ProjectEntity>,
    onProjectClick: (Long) -> Unit,
    onAddClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("프로젝트", style = MaterialTheme.typography.headlineMedium)
                TextButton(onClick = onAddClick) { Text("새로 만들기") }
            }
        }
        item { Text("진행 중", fontWeight = FontWeight.Bold) }
        items(activeProjects, key = { it.id }) { project ->
            ProjectRow(project = project, onClick = { onProjectClick(project.id) })
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { Text("완료", fontWeight = FontWeight.Bold) }
        items(completedProjects, key = { it.id }) { project ->
            ProjectRow(project = project, onClick = { onProjectClick(project.id) })
        }
    }
}

@Composable
private fun ProjectRow(project: ProjectEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(project.title, fontWeight = FontWeight.Bold)
                if (project.isFocus) Text("포커스", color = MaterialTheme.colorScheme.primary)
            }
            if (project.intent.isNotBlank()) {
                Text(project.intent)
            }
            Text("진행률 ${project.progress}%")
        }
    }
}

@Composable
private fun ProjectEditorScreen(
    title: String,
    project: ProjectEntity?,
    onSave: (String, String, Int) -> Unit,
    onCancel: () -> Unit,
) {
    var projectTitle by rememberSaveable { mutableStateOf("") }
    var intent by rememberSaveable { mutableStateOf("") }
    var progress by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(project?.id) {
        if (project != null) {
            projectTitle = project.title
            intent = project.intent
            progress = project.progress
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = projectTitle,
            onValueChange = { projectTitle = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("프로젝트 제목") },
        )
        OutlinedTextField(
            value = intent,
            onValueChange = { intent = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("의도") },
        )
        Text("진행률 ${progress}%")
        Slider(value = progress.toFloat(), onValueChange = { progress = it.toInt() }, valueRange = 0f..100f)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { onSave(projectTitle.trim(), intent.trim(), progress) }, enabled = projectTitle.isNotBlank()) {
                Text("저장")
            }
            TextButton(onClick = onCancel) { Text("취소") }
        }
    }
}

@Composable
private fun ProjectDetailScreen(
    project: ProjectEntity?,
    milestones: List<MilestoneEntity>,
    supportMessage: String,
    onSetFocus: () -> Unit,
    onAddMilestone: (String) -> Unit,
    onEdit: () -> Unit,
    onComplete: () -> Unit,
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("프로젝트를 찾을 수 없다")
        }
        return
    }

    var milestoneTitle by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(project.title, style = MaterialTheme.typography.headlineMedium)
        Text(project.intent.ifBlank { "아직 의도가 입력되지 않았다." })
        Text("진행률 ${project.progress}%")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("응원", fontWeight = FontWeight.Bold)
                Text(supportMessage)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onSetFocus) { Text("포커스로 지정") }
            TextButton(onClick = onEdit) { Text("수정") }
            TextButton(onClick = onComplete) { Text("완료") }
        }
        HorizontalDivider()
        Text("마일스톤", fontWeight = FontWeight.Bold)
        milestones.forEach { milestone ->
            Text("• ${milestone.title}")
        }
        OutlinedTextField(
            value = milestoneTitle,
            onValueChange = { milestoneTitle = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("마일스톤 추가") },
        )
        Button(
            onClick = {
                onAddMilestone(milestoneTitle)
                milestoneTitle = ""
            },
            enabled = milestoneTitle.isNotBlank(),
        ) {
            Text("마일스톤 추가")
        }
    }
}

@Composable
private fun CompleteProjectScreen(
    project: ProjectEntity?,
    onComplete: (String, String) -> Unit,
    onCancel: () -> Unit,
) {
    var achievement by rememberSaveable { mutableStateOf("") }
    var lesson by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("프로젝트 완료", style = MaterialTheme.typography.headlineMedium)
        Text(project?.title ?: "")
        OutlinedTextField(
            value = achievement,
            onValueChange = { achievement = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("성취 요약") },
        )
        OutlinedTextField(
            value = lesson,
            onValueChange = { lesson = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("배운 점") },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { onComplete(achievement.trim(), lesson.trim()) },
                enabled = achievement.isNotBlank() && lesson.isNotBlank(),
            ) {
                Text("레거시 저장")
            }
            TextButton(onClick = onCancel) { Text("취소") }
        }
    }
}

@Composable
private fun LegacyScreen(items: List<LegacyListItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("레거시", style = MaterialTheme.typography.headlineMedium)
        }
        if (items.isEmpty()) {
            item {
                Text("프로젝트를 완료하면 레거시가 시작된다.")
            }
        } else {
            items(items, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(item.projectTitle, fontWeight = FontWeight.Bold)
                        Text("성취: ${item.achievement}")
                        Text("배운 점: ${item.lesson}")
                    }
                }
            }
        }
    }
}
