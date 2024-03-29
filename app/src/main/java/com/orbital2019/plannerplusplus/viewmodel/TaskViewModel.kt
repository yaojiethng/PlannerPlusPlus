package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.DaoAsyncProcessor
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.model.entity.TaskAndSubtask
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.ui.displaytasks.TaskUiModel

// Passing application as context avoids static Activity instance, allows passing of context without retaining a
// reference to an activity. Useful as a ViewModel is supposed to outlast Activities.
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository = PlannerRepository(application)
    private val allTasks: LiveData<List<TaskEntity>> = repository.allTasks
    private val numIncompleteTasks: LiveData<Int> = repository.numIncompleteTasks
    private val numCompletedTasks: LiveData<Int> = repository.numCompletedTasks

    fun insertTask(taskEntity: TaskEntity) {
        repository.insertTask(taskEntity, null)
    }

    fun insertTaskAndSubtask(taskAndSubtask: TaskAndSubtask) {
        // todo make a transaction
        repository.insertTask(taskAndSubtask.task, object : DaoAsyncProcessor.DaoProcessCallback<Long> {
            override fun onResult(result: Long) {
                insertSubtask(*taskAndSubtask.getSubtasks(result))
            }
        })
    }

    fun insertSubtask(vararg subtasks: SubtaskEntity) {
        repository.insertSubTask(*subtasks)
    }

    fun updateTaskandSubtask(taskAndSubtask: TaskAndSubtask) {
        val task = taskAndSubtask.task
        repository.updateTask(task)
        // todo diffutil
        repository.deleteSubtasksByParentId(task.id!!)
        repository.insertSubTask(*taskAndSubtask.getSubtasks(task.id!!))
    }

    fun updateTask(taskEntity: TaskEntity) {
        repository.updateTask(taskEntity)
    }

    fun getSubTasksById(parentId: Long, listener: SubtaskResultListener) {
        return repository.getSubtasks(
            parentId,
            object : DaoAsyncProcessor.DaoProcessCallback<LiveData<List<SubtaskEntity>>> {
                override fun onResult(result: LiveData<List<SubtaskEntity>>) {
                    listener.accept(parentId, result)
                }
            })
    }

    fun setTaskComplete(taskEntity: TaskEntity) {
        repository.setTaskComplete(taskEntity.id!!)
        Log.d("SETCOMPLETE", "TASK SET AS COMPLETE")
    }

    fun setTaskIncomplete(taskEntity: TaskEntity) {
        repository.setTaskIncomplete(taskEntity.id!!)
        Log.d("SETCOMPLETE", "TASK SET AS INCOMPLETE")
    }

    fun setSubtaskComplete(subtask: SubtaskEntity) {
        repository.setSubtaskComplete(subtask.id!!)
        Log.d("SETCOMPLETE", "SUBTASK SET AS COMPLETE")
    }

    fun setSubtaskIncomplete(subtask: SubtaskEntity) {
        repository.setSubtaskIncomplete(subtask)
    }


    fun deleteTask(taskEntity: TaskEntity) {
        repository.deleteTask(taskEntity)
    }

    fun delete(item: ItemModel) {
        if (item is TaskUiModel && item.task != null) {
            repository.deleteTask(item.task)
        }
    }

    fun deleteAllTasks() {
        repository.deleteAllTasks()
    }

    fun getAllTasks(): LiveData<List<TaskEntity>> {
        return allTasks
    }

    fun getNumIncompleteTasks(): LiveData<Int> {
        return numIncompleteTasks
    }

    fun populate() {
        repository.populate()
    }

    interface SubtaskResultListener {
        fun accept(parentId: Long, result: LiveData<List<SubtaskEntity>>)
    }
}