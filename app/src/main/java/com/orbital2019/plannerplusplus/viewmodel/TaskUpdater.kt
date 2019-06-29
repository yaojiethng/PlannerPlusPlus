package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orbital2019.plannerplusplus.model.TaskEntity
import com.orbital2019.plannerplusplus.model.PlannerRepository

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class TaskUpdater(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)

    fun insertTask(task: PlannerTask) {
        repository.insertTask(task.generateEntity())
    }

    fun updateTask(task: PlannerTask) {
        // repo has existing task of this Id
//        if (repository.presentInTable(task.id)) {
        repository.updateTask(task.updateEntity())
//        } else {
//            task.id = null
//            insertTask(task)
//        }
    }

    fun deleteTask(taskEntity: TaskEntity) {
        repository.deleteTask(taskEntity)
    }
}