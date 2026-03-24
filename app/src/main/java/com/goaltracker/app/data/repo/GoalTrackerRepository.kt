package com.goaltracker.app.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.goaltracker.app.data.db.GoalTrackerDatabase
import com.goaltracker.app.data.model.LegacyRecordEntity
import com.goaltracker.app.data.model.LegacyListItem
import com.goaltracker.app.data.model.MilestoneEntity
import com.goaltracker.app.data.model.ProjectEntity
import com.goaltracker.app.data.model.ProjectStatus
import com.goaltracker.app.domain.SupportMessageGenerator
import com.goaltracker.app.widget.WidgetSnapshot
import com.goaltracker.app.widget.WidgetSnapshotStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class HomeState(
    val focusProject: ProjectEntity?,
    val activeProjects: List<ProjectEntity>,
    val supportMessage: String,
)

data class ProjectDetailState(
    val project: ProjectEntity?,
    val milestones: List<MilestoneEntity>,
)

class GoalTrackerRepository private constructor(
    private val database: GoalTrackerDatabase,
    private val widgetSnapshotStore: WidgetSnapshotStore,
) {
    private val dao = database.projectDao()

    val activeProjects: Flow<List<ProjectEntity>> = dao.observeActiveProjects()
    val completedProjects: Flow<List<ProjectEntity>> = dao.observeCompletedProjects()
    val legacyRecords: Flow<List<LegacyRecordEntity>> = dao.observeLegacyRecords()
    val legacyListItems: Flow<List<LegacyListItem>> = dao.observeLegacyListItems()

    val homeState: Flow<HomeState> = combine(
        dao.observeFocusProject(),
        dao.observeActiveProjects(),
        dao.observeCompletedCount(),
        dao.observeLatestLegacy(),
    ) { focus, active, completedCount, latestLegacy ->
        HomeState(
            focusProject = focus,
            activeProjects = active,
            supportMessage = SupportMessageGenerator.fromLegacy(completedCount, latestLegacy?.lesson),
        )
    }

    fun observeProjectDetail(projectId: Long): Flow<ProjectDetailState> = combine(
        dao.observeProject(projectId),
        dao.observeMilestones(projectId),
    ) { project, milestones ->
        ProjectDetailState(project = project, milestones = milestones)
    }

    suspend fun createProject(title: String, intent: String, progress: Int) {
        val now = System.currentTimeMillis()
        dao.insertProject(
            ProjectEntity(
                title = title,
                intent = intent,
                progress = progress.coerceIn(0, 100),
                createdAt = now,
                updatedAt = now,
            ),
        )
        refreshWidgetSnapshot()
    }

    suspend fun updateProject(project: ProjectEntity, title: String, intent: String, progress: Int) {
        dao.updateProject(
            project.copy(
                title = title,
                intent = intent,
                progress = progress.coerceIn(0, 100),
                updatedAt = System.currentTimeMillis(),
            ),
        )
        refreshWidgetSnapshot()
    }

    suspend fun setFocusProject(project: ProjectEntity) {
        database.withTransaction {
            dao.clearFocus()
            dao.updateProject(project.copy(isFocus = true, updatedAt = System.currentTimeMillis()))
        }
        refreshWidgetSnapshot()
    }

    suspend fun addMilestone(projectId: Long, title: String) {
        if (title.isBlank()) return
        dao.insertMilestone(
            MilestoneEntity(
                projectId = projectId,
                title = title.trim(),
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun completeProject(project: ProjectEntity, achievement: String, lesson: String) {
        val now = System.currentTimeMillis()
        database.withTransaction {
            dao.updateProject(
                project.copy(
                    status = ProjectStatus.COMPLETED.name,
                    isFocus = false,
                    progress = 100,
                    updatedAt = now,
                    completedAt = now,
                ),
            )
            dao.insertLegacyRecord(
                LegacyRecordEntity(
                    projectId = project.id,
                    achievement = achievement.trim(),
                    lesson = lesson.trim(),
                    createdAt = now,
                ),
            )
        }
        refreshWidgetSnapshot()
    }

    suspend fun refreshWidgetSnapshot() {
        val focus = dao.getFocusProjectNow()
        val completedCount = dao.getCompletedCountNow()
        val latestLegacy = dao.getLatestLegacyNow()
        widgetSnapshotStore.write(
            WidgetSnapshot(
                title = focus?.title ?: "포커스 프로젝트 없음",
                progress = focus?.progress ?: 0,
                supportMessage = SupportMessageGenerator.fromLegacy(completedCount, latestLegacy?.lesson),
            ),
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: GoalTrackerRepository? = null

        fun getInstance(context: Context): GoalTrackerRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GoalTrackerRepository(
                    database = GoalTrackerDatabase.getInstance(context),
                    widgetSnapshotStore = WidgetSnapshotStore(context.applicationContext),
                ).also { INSTANCE = it }
            }
        }
    }
}
