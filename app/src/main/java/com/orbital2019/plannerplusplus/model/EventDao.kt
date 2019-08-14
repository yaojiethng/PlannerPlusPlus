package com.orbital2019.plannerplusplus.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.model.entity.EventTaskRequirement
import com.orbital2019.plannerplusplus.model.entity.TaskEntity

@Dao
interface EventDao {

    // highlight any of the tags and press ctrl-b for advanced tagging options
    @Insert
    fun insert(eventEntity: EventEntity): Long

    @Update
    fun update(eventEntity: EventEntity)

    @Delete
    fun delete(eventEntity: EventEntity)

    @Query("SELECT * FROM event_table WHERE id = :id")
    fun findById(id: Int): LiveData<List<EventEntity>>

    // @Query allows custom queries to be defined
    @Query("DELETE FROM event_table")
    fun deleteAllEvents()

    @Query("DELETE FROM task_requirement_by_event")
    fun deleteAllRequirements()

    /**
     * Queries for list of all EventAndRelatedTasks, ordered by:
     *      datetime(startTime), followed by datetime(endTime), followed by id.
     * If the result of the query is a Pojo with Relation fields, these fields are queried separately.
     * To receive consistent results between these queries, you probably want to run them in a single transaction.
     * @return LiveData<List<EventAndRelatedTasks>> to get an Observable Relation Pojo linking Event with its related Tasks
     */
    @Transaction
    @Query("SELECT * FROM event_table ORDER BY datetime(eventStartTime), datetime(eventEndTime), id DESC")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Transaction
    @Query(
        """
        SELECT task_table.* FROM task_table
        INNER JOIN task_requirement_by_event event_required_tasks ON event_required_tasks.task_id = task_table.id
        INNER JOIN event_table event ON event_required_tasks.event_id = event.id
        WHERE event.id = :eventId
        ORDER BY datetime(event.eventStartTime), datetime(event.eventEndTime), id DESC 
        """
    )
    fun getRelatedTasksByEventId(eventId: Long): LiveData<List<TaskEntity>>

    @Transaction
    fun setEventRequirement(eventId: Long, vararg taskIds: Long) {
        removeEventRequirement(eventId)
        for (taskId: Long in taskIds) {
            addEventRequirement(EventTaskRequirement(eventId, taskId))
        }

    }

    @Query("DELETE FROM task_requirement_by_event WHERE event_id = :eventId")
    fun removeEventRequirement(eventId: Long)

    @Insert
    fun addEventRequirement(eventTaskRequirement: EventTaskRequirement)

}