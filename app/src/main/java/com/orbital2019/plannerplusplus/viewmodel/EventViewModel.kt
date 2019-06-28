package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.EventEntity
import com.orbital2019.plannerplusplus.model.EventRepository

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository =
        EventRepository(application)
    private val allEvents: LiveData<List<EventEntity>> = repository.allEvents

    fun insertEvent(eventEntity: EventEntity) {
        repository.insert(eventEntity)
    }

    fun updateEvent(eventEntity: EventEntity) {
        repository.update(eventEntity)
    }

    fun deleteEvent(eventEntity: EventEntity) {
        repository.delete(eventEntity)
    }

    fun deleteAllEvents() {
        repository.deleteAllEvents()
    }

    fun getAllEvents(): LiveData<List<EventEntity>> {
        return allEvents
    }
}