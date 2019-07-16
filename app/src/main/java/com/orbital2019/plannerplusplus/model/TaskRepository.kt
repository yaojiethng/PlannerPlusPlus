package com.orbital2019.plannerplusplus.model

interface TaskRepository {
    var taskDao: TaskDao

    fun insertTask(taskEntity: TaskEntity) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.insert(taskEntity)
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
        val id: Int = taskEntity.id!!
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.setComplete(id)
            }
        }.start()
    }

    fun setTaskIncomplete(taskEntity: TaskEntity) {
        val id: Int = taskEntity.id!!
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                taskDao.setIncomplete(id)
            }
        }.start()
    }
}