package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orbital2019.plannerplusplus.model.EventRepository
import com.orbital2019.plannerplusplus.model.PlannerEvent

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository =
        EventRepository(application)
    private val allEvents: LiveData<List<PlannerEvent>> = repository.allEvents

    fun insertEvent(plannerEvent: PlannerEvent) {
        repository.insert(plannerEvent)
    }

    fun updateEvent(plannerEvent: PlannerEvent) {
        repository.update(plannerEvent)
    }

    fun deleteEvent(plannerEvent: PlannerEvent) {
        repository.delete(plannerEvent)
    }

    fun deleteAllEvents() {
        repository.deleteAllEvents()
    }

    fun getAllEvents(): LiveData<List<PlannerEvent>> {
        return allEvents
    }
}