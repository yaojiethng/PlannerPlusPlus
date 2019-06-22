package com.orbital2019.plannerplusplus

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

//TODO: check if this can be done using java's ExecutorService

class EventRepository {
    private lateinit var eventDao: EventDao
    private lateinit var allEvents: LiveData<List<Event>>

    fun noteRepository(application: Application) {
        val database: AppDatabase = AppDatabase.getInstance(application)!!
        eventDao = database.eventDao()
        allEvents = eventDao.getAllEvents()
    }

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

        override fun doInBackground(vararg events: Event?) : Void? {
            events[0]?.let { eventDao.update(it) }
            return null
        }
    }

    class DeleteEventAsyncTask constructor(var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg events: Event?) : Void? {
            events[0]?.let { eventDao.delete(it) }
            return null
        }
    }

    class DeleteAllEventsAsyncTask constructor(var eventDao: EventDao) : AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg params: Event?): Void? {
            eventDao.deleteAllEvents()
            return null
        }
    }
}