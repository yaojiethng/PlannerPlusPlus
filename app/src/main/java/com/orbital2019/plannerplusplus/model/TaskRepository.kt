package com.orbital2019.plannerplusplus.model

import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.model.entity.TaskEntity

interface TaskRepository {
    var taskDao: TaskDao

    fun insertTask(taskEntity: TaskEntity, callback: DaoAsyncProcessor.DaoProcessCallback<Long>?) {
        object : DaoAsyncProcessor<Long>(callback) {
            override fun doAsync(): Long {
                return taskDao.insert(taskEntity)
            }
        }.start()
    }

    fun insertSubTask(vararg subtaskEntities: SubtaskEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.insert(*subtaskEntities)
            }
        }.start()
    }

    fun updateTask(taskEntity: TaskEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.update(taskEntity)
            }
        }.start()
    }


    fun deleteTask(taskEntity: TaskEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.delete(taskEntity)
            }
        }.start()
    }

    fun deleteAllTasks() {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.deleteAllTasks()
            }
        }.start()
    }

    fun setTaskComplete(taskEntity: TaskEntity) {
        val id: Long = taskEntity.id!!
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.setComplete(id)
            }
        }.start()
    }

    fun setTaskIncomplete(taskEntity: TaskEntity) {
        val id: Long = taskEntity.id!!
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.setIncomplete(id)
            }
        }.start()
    }
}