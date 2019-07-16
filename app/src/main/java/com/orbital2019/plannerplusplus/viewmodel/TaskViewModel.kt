package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.TaskEntity

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)
    private val incompleteTasks: LiveData<List<TaskEntity>> = repository.incompleteTasks
    private val completedTasks: LiveData<List<TaskEntity>> = repository.completedTasks

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

    fun getCompletedTasks(): LiveData<List<TaskEntity>> {
        return completedTasks
    }

    fun getIncompleteTasks(): LiveData<List<TaskEntity>> {
        return incompleteTasks
    }
}