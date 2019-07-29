package com.orbital2019.plannerplusplus.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.orbital2019.plannerplusplus.model.entity.EventAndRelatedTasks
import com.orbital2019.plannerplusplus.model.entity.EventEntity

@Dao
interface EventDao {

    // highlight any of the tags and press ctrl-b for advanced tagging options
    @Insert
    fun insert(eventEntity: EventEntity)

    @Update
    fun update(eventEntity: EventEntity)

    @Delete
    fun delete(eventEntity: EventEntity)

    @Query("SELECT * FROM event_table WHERE id = :id")
    fun findById(id: Int): LiveData<List<EventEntity>>

    // @Query allows custom queries to be defined
    @Query("DELETE FROM event_table")
    fun deleteAllEvents()

    /**
     * Queries for list of all EventAndRelatedTasks, ordered by:
     *      datetime(startTime), followed by datetime(endTime), followed by id.
     * If the result of the query is a Pojo with Relation fields, these fields are queried separately.
     * To receive consistent results between these queries, you probably want to run them in a single transaction.
     * @return LiveData<List<EventAndRelatedTasks>> to get an Observable Relation Pojo linking Event with its related Tasks
     */
    @Transaction
    @Query("SELECT * FROM event_table ORDER BY datetime(eventStartTime), datetime(eventEndTime), id DESC")
    fun getAllEvents(): LiveData<List<EventAndRelatedTasks>>

}