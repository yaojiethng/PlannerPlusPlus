package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orbital2019.plannerplusplus.model.EventEntity
import com.orbital2019.plannerplusplus.model.PlannerRepository

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventUpdater(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)

    fun insertEvent(event: PlannerEvent) {
        repository.insertEvent(event.generateEntity())
    }

    fun updateEvent(event: PlannerEvent) {
        // repo has existing event of this Id
//        if (repository.presentInTable(event.id)) {
        repository.updateEvent(event.updateEntity())
//        } else {
//            event.id = null
//            insertEvent(event)
//        }
    }

    fun deleteEvent(eventEntity: EventEntity) {
        repository.deleteEvent(eventEntity)
    }
}