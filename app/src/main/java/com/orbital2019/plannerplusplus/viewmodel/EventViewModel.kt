package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.DaoAsyncProcessor
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.ui.displayevents.EventUiModel

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)
    private val allEvents: LiveData<List<EventEntity>> = repository.allEvents

    fun insertEvent(eventEntity: EventEntity, vararg taskIds: Long) {
        repository.insertEvent(eventEntity, object : DaoAsyncProcessor.DaoProcessCallback<Long> {
            override fun onResult(result: Long) {
                setEventRequirement(result, *taskIds)
            }
        })
    }

    fun setEventRequirement(eventId: Long, vararg taskIds: Long) {
        repository.setEventRequirement(eventId, *taskIds)
    }

    fun updateEvent(eventEntity: EventEntity, vararg taskIds: Long) {
        repository.updateEvent(eventEntity)
        repository.setEventRequirement(eventEntity.id!!, *taskIds)
    }

    fun delete(model: ItemModel) {
        if (model is EventUiModel) {
            repository.deleteEvent(model.event!!)
        }
    }

    fun deleteAllEvents() {
        repository.deleteAllEvents()
    }

    fun getAllEvents(): LiveData<List<EventEntity>> {
        return allEvents
    }

    fun getRelatedTasksByEventId(parentId: Long?, listener: (LiveData<List<TaskEntity>>) -> Unit) {
        return if (parentId != null) repository.getRelatedTasksByEventId(
            parentId,
            object : DaoAsyncProcessor.DaoProcessCallback<LiveData<List<TaskEntity>>> {
                override fun onResult(result: LiveData<List<TaskEntity>>) {
                    listener(result)
                }
            })
        else Unit
    }

}