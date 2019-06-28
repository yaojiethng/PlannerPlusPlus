package com.orbital2019.plannerplusplus


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository = EventRepository(application)
    private val allEvents: LiveData<List<Event>> = repository.allEvents

    fun insertEvent(event: Event) {
        repository.insert(event)
    }

    fun updateEvent(event: Event) {
        repository.update(event)
    }

    fun deleteEvent(event: Event) {
        repository.delete(event)
    }

    fun deleteAllEvents() {
        repository.deleteAllEvents()
    }

    fun getAllEvents(): LiveData<List<Event>> {
        return allEvents
    }
}