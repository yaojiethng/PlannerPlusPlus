package com.orbital2019.plannerplusplus

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

//TODO: check if this can be done using java's ExecutorService

class EventRepository(application: Application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }
    private var eventDao: EventDao = database.eventDao()
    internal var allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    // has to be manually executed on background thread to prevent app from crashing
    fun insert(event: Event) {
        InsertEventAsyncTask(eventDao).execute(event)
    }

    fun update(event: Event) {
        UpdateEventAsyncTask(eventDao).execute(event)
    }

    fun delete(event: Event) {
        DeleteEventAsyncTask(eventDao).execute(event)
    }

    fun deleteAllEvents() {
        DeleteAllEventsAsyncTask(eventDao).execute()
    }

    class InsertEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg events: Event?): Void? {
            events[0]?.let { eventDao.insert(it) }
            return null
        }
    }

    class UpdateEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg events: Event?): Void? {
            events[0]?.let { eventDao.update(it) }
            return null
        }
    }

    class DeleteEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg events: Event?): Void? {
            events[0]?.let { eventDao.delete(it) }
            return null
        }
    }

    class DeleteAllEventsAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg params: Event?): Void? {
            eventDao.deleteAllEvents()
            return null
        }
    }
}