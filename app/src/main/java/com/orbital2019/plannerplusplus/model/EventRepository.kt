package com.orbital2019.plannerplusplus.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

//TODO: check if this can be done using java's ExecutorService

class EventRepository(application: Application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }
    private var eventDao: EventDao = database.eventDao()
    internal var allEvents: LiveData<List<PlannerEvent>> = eventDao.getAllEvents()

    // has to be manually executed on background thread to prevent app from crashing
    fun insert(plannerEvent: PlannerEvent) {
        InsertEventAsyncTask(eventDao).execute(plannerEvent)
    }

    fun update(plannerEvent: PlannerEvent) {
        UpdateEventAsyncTask(eventDao).execute(plannerEvent)
    }

    fun delete(plannerEvent: PlannerEvent) {
        DeleteEventAsyncTask(eventDao).execute(plannerEvent)
    }

    fun deleteAllEvents() {
        DeleteAllEventsAsyncTask(eventDao).execute()
    }

    class InsertEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<PlannerEvent, Void, Void>() {

        override fun doInBackground(vararg plannerEvents: PlannerEvent?): Void? {
            plannerEvents[0]?.let { eventDao.insert(it) }
            return null
        }
    }

    class UpdateEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<PlannerEvent, Void, Void>() {

        override fun doInBackground(vararg plannerEvents: PlannerEvent?): Void? {
            plannerEvents[0]?.let { eventDao.update(it) }
            return null
        }
    }

    class DeleteEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<PlannerEvent, Void, Void>() {

        override fun doInBackground(vararg plannerEvents: PlannerEvent?): Void? {
            plannerEvents[0]?.let { eventDao.delete(it) }
            return null
        }
    }

    class DeleteAllEventsAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<PlannerEvent, Void, Void>() {

        override fun doInBackground(vararg params: PlannerEvent?): Void? {
            eventDao.deleteAllEvents()
            return null
        }
    }
}