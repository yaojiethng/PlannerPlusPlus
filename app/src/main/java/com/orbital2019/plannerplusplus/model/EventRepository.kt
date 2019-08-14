package com.orbital2019.plannerplusplus.model

import com.orbital2019.plannerplusplus.model.entity.EventEntity

interface EventRepository {
    var eventDao: EventDao

    fun insertEvent(eventEntity: EventEntity, callback: DaoAsyncProcessor.DaoProcessCallback<Long>?) {
        object : DaoAsyncProcessor<Long>(callback) {
            override fun doAsync(): Long {
                return eventDao.insert(eventEntity)
            }
        }.start()
    }

    fun updateEvent(eventEntity: EventEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                eventDao.update(eventEntity)
            }
        }.start()
    }


    fun deleteEvent(eventEntity: EventEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                eventDao.delete(eventEntity)
            }
        }.start()
    }

    fun deleteAllEvents() {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                eventDao.deleteAllEvents()
            }
        }.start()
    }

    fun setEventRequirement(eventId: Long, vararg taskIds: Long) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                eventDao.setEventRequirement(eventId, *taskIds)
            }
        }.start()
    }
}