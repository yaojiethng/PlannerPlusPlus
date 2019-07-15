/** PlannerRepository is the repository for the planner app
 * A repository has the task of managing the retrieval of data from multiple sources (offline and online databases)
 * In this case, the only database is the offline database (which is the AppDatabase class)
 */

package com.orbital2019.plannerplusplus.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

// TODO: check if this can be done using java's ExecutorService
// TODO: separate event and task management into different interfaces
// TODO: single asyncTask constructor instead of this copy pasted mess

class PlannerRepository(application: Application) {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }
    private var eventDao: EventDao = database.eventDao()
    private var taskDao: TaskDao = database.taskDao()
    internal var allEvents: LiveData<List<EventEntity>> = eventDao.getAllEvents()
    internal var allTasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks()

    // CRUD operations on the database have to be manually executed on background thread to prevent app from crashing
    fun insertEvent(eventEntity: EventEntity) {
        InsertEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun updateEvent(eventEntity: EventEntity) {
        UpdateEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun deleteEvent(eventEntity: EventEntity) {
        DeleteEventAsyncTask(eventDao).execute(eventEntity)
    }

    fun deleteAllEvents() {
        DeleteAllEventsAsyncTask(eventDao).execute()
    }

    fun insertTask(taskEntity: TaskEntity) {
        InsertTaskAsyncTask(taskDao).execute(taskEntity)
    }

    fun updateTask(taskEntity: TaskEntity) {
        UpdateTaskAsyncTask(taskDao).execute(taskEntity)
    }

    fun setTaskComplete(taskEntity: TaskEntity) {
        SetTaskCompleteAsyncTask(taskDao).execute(taskEntity)
    }

    fun setTaskIncomplete(taskEntity: TaskEntity) {
        SetTaskIncompleteAsyncTask(taskDao).execute(taskEntity)
    }

    fun deleteTask(taskEntity: TaskEntity) {
        DeleteTaskAsyncTask(taskDao).execute(taskEntity)
    }

    fun deleteAllTasks() {
        DeleteAllTasksAsyncTask(taskDao).execute()
    }

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

    class InsertTaskAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {

        override fun doInBackground(vararg taskEntities: TaskEntity?): Void? {
            taskEntities[0]?.let { taskDao.insert(it) }
            return null
        }
    }

    class UpdateTaskAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {

        override fun doInBackground(vararg taskEntities: TaskEntity?): Void? {
            taskEntities[0]?.let { taskDao.update(it) }
            return null
        }
    }

    class SetTaskCompleteAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {
        override fun doInBackground(vararg p0: TaskEntity?): Void? {
            val id: Int = p0[0]!!.id!!
            taskDao.setComplete(id)
            return null
        }

    }

    class SetTaskIncompleteAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {
        override fun doInBackground(vararg p0: TaskEntity?): Void? {
            val id: Int = p0[0]!!.id!!
            taskDao.setIncomplete(id)
            return null
        }
    }

    class DeleteTaskAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {

        override fun doInBackground(vararg taskEntities: TaskEntity?): Void? {
            taskEntities[0]?.let { taskDao.delete(it) }
            return null
        }
    }

    class DeleteAllTasksAsyncTask constructor(private var taskDao: TaskDao) : AsyncTask<TaskEntity, Void, Void>() {

        override fun doInBackground(vararg params: TaskEntity?): Void? {
            taskDao.deleteAllTasks()
            return null
        }
    }
}