package com.example.android.architecture.blueprints.todoapp.tasks

import app.junhyounglee.archroid.runtime.core.view.MvpView
import com.example.android.architecture.blueprints.todoapp.data.Task

interface TasksContract {

    interface TasksView : MvpView {

        var isActive: Boolean

        fun setLoadingIndicator(active: Boolean)

        fun showTasks(tasks: List<Task>)

        fun showAddTask()

        fun showTaskDetailsUi(taskId: String)

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()

        fun showCompletedTasksCleared()

        fun showLoadingTasksError()

        fun showNoTasks()

        fun showActiveFilterLabel()

        fun showCompletedFilterLabel()

        fun showAllFilterLabel()

        fun showNoActiveTasks()

        fun showNoCompletedTasks()

        fun showSuccessfullySavedMessage()

        fun showFilteringPopUpMenu()
    }

    interface Presenter {

        var currentFiltering: TasksFilterType

        fun result(requestCode: Int, resultCode: Int)

        fun loadTasks(forceUpdate: Boolean)

        fun addNewTask()

        fun openTaskDetails(requestedTask: Task)

        fun completeTask(completedTask: Task)

        fun activateTask(activeTask: Task)

        fun clearCompletedTasks()
    }
}