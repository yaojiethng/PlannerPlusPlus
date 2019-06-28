package com.orbital2019.plannerplusplus.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDao {

    // highlight any of the tags and press ctrl-b for advanced tagging options
    @Insert
    fun insert(plannerEvent: PlannerEvent)

    @Update
    fun update(plannerEvent: PlannerEvent)

    @Delete
    fun delete(plannerEvent: PlannerEvent)

    // @Query allows custom queries to be defined
    @Query("DELETE FROM event_table")
    fun deleteAllEvents()

    // can use object type LiveData<List<Note>> to get an observable object
    @Query("SELECT * FROM event_table ORDER BY id DESC")
    fun getAllEvents(): LiveData<List<PlannerEvent>>
}