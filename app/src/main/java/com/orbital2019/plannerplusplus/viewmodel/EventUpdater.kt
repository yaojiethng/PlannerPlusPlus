package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orbital2019.plannerplusplus.model.EventEntity
import com.orbital2019.plannerplusplus.model.EventRepository
import com.orbital2019.plannerplusplus.model.PlannerEvent

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventUpdater(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository =
        EventRepository(application)

    fun insertEvent(event: PlannerEvent) {
        repository.insert(event.generateEntity())
    }

    fun updateEvent(eventEntity: EventEntity) {
        repository.update(eventEntity)
    }

    fun deleteEvent(eventEntity: EventEntity) {
        repository.delete(eventEntity)
    }
}