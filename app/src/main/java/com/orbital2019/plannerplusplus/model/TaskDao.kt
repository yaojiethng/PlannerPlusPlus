/**
 * Data Access Object defining database interactions for Tasks, intended for use with the TasksFragment fragment in
 * MainActivity.
 */
package com.orbital2019.plannerplusplus.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.model.entity.TaskEntity

@Dao
interface TaskDao {

    // highlight any of the tags and press ctrl-b for advanced tagging options
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg subtaskEntities: SubtaskEntity)

    @Update
    fun update(taskEntity: TaskEntity)

    @Query("UPDATE task_table SET isComplete = 1 WHERE id = :id")
    fun setComplete(id: Long)

    @Query("UPDATE subtask_table SET isComplete = 1 WHERE parentId = :parentId")
    fun setSubtaskCompleteByParent(parentId: Long)

    @Query("UPDATE task_table SET isComplete = 0 WHERE id = :id")
    fun setIncomplete(id: Long)

    @Query("UPDATE subtask_table SET isComplete = 1 WHERE id = :id")
    fun setSubtaskComplete(id: Long)

    @Query("UPDATE subtask_table SET isComplete = 0 WHERE id = :id")
    fun setSubtaskIncomplete(id: Long)

    @Delete
    fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun findById(id: Int): List<TaskEntity>

    // @Query allows custom queries to be defined
    @Query("DELETE FROM task_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM task_table ORDER BY isComplete ASC, id DESC")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM subtask_table WHERE parentId = :taskId ORDER BY id DESC")
    fun getSubtasks(taskId: Long): LiveData<List<SubtaskEntity>>

    @Query("SELECT * FROM task_table WHERE isComplete = 0 ORDER BY id DESC")
    fun getIncompleteTasks(): LiveData<List<TaskEntity>>

    @Query("SELECT count(*) FROM task_table WHERE isComplete = 0")
    fun countIncompleteTasks(): LiveData<Int>

    @Query("SELECT * FROM task_table WHERE isComplete = 1 ORDER BY id DESC")
    fun getCompletedTasks(): LiveData<List<TaskEntity>>

    @Query("SELECT count(*) FROM task_table WHERE isComplete = 1")
    fun countCompletedTasks(): LiveData<Int>
}