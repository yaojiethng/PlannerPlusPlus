package com.orbital2019.plannerplusplus.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orbital2019.plannerplusplus.model.PlannerRepository
import com.orbital2019.plannerplusplus.model.entity.EventEntity

// avoids static Activity instance, allows passing of context without retaining a reference to an activity
class EventUpdater(application: Application) : AndroidViewModel(application) {

    private val repository: PlannerRepository =
        PlannerRepository(application)

    fun insertEvent(event: EventEntity) {
        repository.insertEvent(event)
    }

    fun updateEvent(event: EventEntity) {
        repository.updateEvent(event)
    }
}