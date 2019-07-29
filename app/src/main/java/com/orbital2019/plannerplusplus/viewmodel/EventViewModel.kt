package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.entity.EventAndRelatedTasks
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.ui.displayevents.EventUiModel

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)
    private val allEvents: LiveData<List<EventAndRelatedTasks>> = repository.allEvents

    fun insertEvent(eventEntity: EventEntity) {
        repository.insertEvent(eventEntity)
    }

    fun updateEvent(eventEntity: EventEntity) {
        repository.updateEvent(eventEntity)
    }

    fun delete(model: ItemModel) {
        if (model is EventUiModel) {
            repository.deleteEvent(model.event!!)
        }
    }

    fun deleteAllEvents() {
        repository.deleteAllEvents()
    }

    fun getAllEvents(): LiveData<List<EventAndRelatedTasks>> {
        return allEvents
    }
}