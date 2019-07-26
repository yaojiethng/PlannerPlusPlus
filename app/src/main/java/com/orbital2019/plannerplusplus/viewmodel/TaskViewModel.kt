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
import com.orbital2019.plannerplusplus.view.ui.displaytasks.SubtaskUiModel
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
        repository.insertTask(taskAndSubtask.task, object : DaoAsyncProcessor.DaoProcessCallback<Long> {
            override fun onResult(result: Long) {
                insertSubtask(*taskAndSubtask.getSubtasks(result))
            }
        })
    }

    fun insertSubtask(vararg subtasks: SubtaskEntity) {
        repository.insertSubTask(*subtasks)
    }

    fun updateTask(taskEntity: TaskEntity) {
        repository.updateTask(taskEntity)
    }

    fun getSubTasks(taskEntity: TaskEntity, listener: SubtaskResultListener) {
        return repository.getSubtasks(
            taskEntity.id!!,
            object : DaoAsyncProcessor.DaoProcessCallback<LiveData<List<SubtaskEntity>>> {
                override fun onResult(result: LiveData<List<SubtaskEntity>>) {
                    listener.accept(result)
                }
            })
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
        insertTaskAndSubtask(
            TaskAndSubtask(
                TaskEntity("DB_POP1", "", false, "", false),
                SubtaskUiModel(null, "SUBTASK1", false, null),
                SubtaskUiModel(null, "SUBTASK2", false, null),
                SubtaskUiModel(null, "SUBTASK3", false, null)
            )
        )
    }

    interface SubtaskResultListener {
        fun accept(result: LiveData<List<SubtaskEntity>>)
    }
}