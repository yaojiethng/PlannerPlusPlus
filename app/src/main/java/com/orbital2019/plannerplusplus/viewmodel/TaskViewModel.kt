package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.TaskEntity

// Passing application as context avoids static Activity instance, allows passing of context without retaining a
// reference to an activity. Useful as a ViewModel is supposed to outlast Activities.
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)
    private val allTasks: LiveData<List<TaskEntity>> = repository.allTasks
    private val numIncompleteTasks: LiveData<Int> = repository.numIncompleteTasks
    private val numCompletedTasks: LiveData<Int> = repository.numCompletedTasks

    fun insertTask(taskEntity: TaskEntity) {
        repository.insertTask(taskEntity)
    }

    fun updateTask(taskEntity: TaskEntity) {
        repository.updateTask(taskEntity)
    }

    fun setTaskComplete(taskEntity: TaskEntity) {
        repository.setTaskComplete(taskEntity)
        Log.d("SETCOMPLETE", "TASK SET AS COMPLETE")
    }

    fun setTaskIncomplete(taskEntity: TaskEntity) {
        repository.setTaskIncomplete(taskEntity)
        Log.d("SETCOMPLETE", "TASK SET AS INCOMPLETE")
    }


    fun deleteTask(taskEntity: TaskEntity) {
        repository.deleteTask(taskEntity)
    }

    fun deleteAllTasks() {
        repository.deleteAllTasks()
    }

    fun getAllTasks(): LiveData<List<TaskEntity>> {
        return allTasks
    }

}