package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.entity.TaskEntity

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class TaskUpdater(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)

    fun insertTask(task: TaskEntity) {
        repository.insertTask(task)
    }

    fun updateTask(task: TaskEntity) {
        repository.updateTask(task)
    }

    fun deleteTask(taskEntity: TaskEntity) {
        repository.deleteTask(taskEntity)
    }
}