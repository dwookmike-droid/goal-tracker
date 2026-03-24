package com.goaltracker.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.goaltracker.app.data.model.ProjectEntity
import com.goaltracker.app.data.repo.GoalTrackerRepository
import com.goaltracker.app.data.repo.HomeState
import com.goaltracker.app.data.repo.ProjectDetailState
import com.goaltracker.app.widget.FocusProjectWidgetReceiver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalTrackerViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val repository = GoalTrackerRepository.getInstance(application)

    val homeState: StateFlow<HomeState> = repository.homeState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeState(
            focusProject = null,
            activeProjects = emptyList(),
            supportMessage = "Start your first project and build your legacy.",
        ),
    )

    val completedProjects = repository.completedProjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    val legacyItems = repository.legacyListItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    fun projectDetail(projectId: Long) = repository.observeProjectDetail(projectId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProjectDetailState(
            project = null,
            milestones = emptyList(),
        ),
    )

    fun createProject(title: String, intent: String, progress: Int) {
        viewModelScope.launch {
            repository.createProject(title, intent, progress)
            refreshWidget()
        }
    }

    fun updateProject(project: ProjectEntity, title: String, intent: String, progress: Int) {
        viewModelScope.launch {
            repository.updateProject(project, title, intent, progress)
            refreshWidget()
        }
    }

    fun setFocusProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.setFocusProject(project)
            refreshWidget()
        }
    }

    fun addMilestone(projectId: Long, title: String) {
        viewModelScope.launch {
            repository.addMilestone(projectId, title)
        }
    }

    fun completeProject(project: ProjectEntity, achievement: String, lesson: String) {
        viewModelScope.launch {
            repository.completeProject(project, achievement, lesson)
            refreshWidget()
        }
    }

    private fun refreshWidget() {
        FocusProjectWidgetReceiver.updateAll(getApplication())
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GoalTrackerViewModel(application) as T
                }
            }
        }
    }
}
