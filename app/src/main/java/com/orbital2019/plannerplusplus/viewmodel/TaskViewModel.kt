package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.TaskEntity
import com.orbital2019.plannerplusplus.model.PlannerRepository

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)
    private val allTasks: LiveData<List<TaskEntity>> = repository.allTasks

    fun insertTask(taskEntity: TaskEntity) {
        repository.insertTask(taskEntity)
    }

    fun updateTask(taskEntity: TaskEntity) {
        repository.updateTask(taskEntity)
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