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
    internal var allEvents: LiveData<List<EventEntity>> = eventDao.getAllEvents()

    // has to be manually executed on background thread to prevent app from crashing
    fun insert(event: EventEntity) {
        InsertEventAsyncTask(eventDao).execute(event)
    }

    fun update(event: EventEntity) {
        UpdateEventAsyncTask(eventDao).execute(event)
    }

    fun delete(event: EventEntity) {
        DeleteEventAsyncTask(eventDao).execute(event)
    }

    fun deleteAllEvents() {
        DeleteAllEventsAsyncTask(eventDao).execute()
    }

    class InsertEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg events: EventEntity?): Void? {
            events[0]?.let { eventDao.insert(it) }
            return null
        }
    }

    class UpdateEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg events: EventEntity?): Void? {
            events[0]?.let { eventDao.update(it) }
            return null
        }
    }

    class DeleteEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg events: EventEntity?): Void? {
            events[0]?.let { eventDao.delete(it) }
            return null
        }
    }

    class DeleteAllEventsAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg params: EventEntity?): Void? {
            eventDao.deleteAllEvents()
            return null
        }
    }
}