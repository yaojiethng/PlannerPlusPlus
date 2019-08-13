package com.orbital2019.plannerplusplus.model

import com.orbital2019.plannerplusplus.model.entity.EventEntity

interface EventRepository {
    var eventDao: EventDao

    fun insertEvent(eventEntity: EventEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                eventDao.insert(eventEntity)
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
}