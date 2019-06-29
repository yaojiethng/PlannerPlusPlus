package com.orbital2019.plannerplusplus.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    // highlight any of the tags and press ctrl-b for advanced tagging options
    @Insert
    fun insert(taskEntity: TaskEntity)

    @Update
    fun update(taskEntity: TaskEntity)

    @Delete
    fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun findById(id: Int): List<TaskEntity>

    // @Query allows custom queries to be defined
    @Query("DELETE FROM task_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<TaskEntity>>
}