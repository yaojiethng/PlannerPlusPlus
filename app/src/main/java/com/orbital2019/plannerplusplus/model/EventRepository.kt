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
    fun insert(eventEntity: EventEntity) {
        InsertEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun update(eventEntity: EventEntity) {
        UpdateEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun delete(eventEntity: EventEntity) {
        DeleteEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun deleteAllEvents() {
        DeleteAllEventsAsyncTask(eventDao).execute()
    }

//    fun presentInTable(id: Int?): Boolean {
//        return if (id != null) FindOneEventAsyncTask(eventDao).execute(id).get()!= null else false
//    }

    class InsertEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg eventEntities: EventEntity?): Void? {
            eventEntities[0]?.let { eventDao.insert(it) }
            return null
        }
    }

    class UpdateEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg eventEntities: EventEntity?): Void? {
            eventEntities[0]?.let { eventDao.update(it) }
            return null
        }
    }

//    class FindOneEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<Int, Void, EventEntity>() {
//
//        override fun doInBackground(vararg p0: Int?): EventEntity {
//            return p0[0]!!.let { eventDao.findOne(it) }
//        }
//    }

    class DeleteEventAsyncTask constructor(private var eventDao: EventDao) : AsyncTask<EventEntity, Void, Void>() {

        override fun doInBackground(vararg eventEntities: EventEntity?): Void? {
            eventEntities[0]?.let { eventDao.delete(it) }
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