package com.orbital2019.plannerplusplus.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtask_table")
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String = "",
    var isComplete: Boolean = false,
    var parentId: Long
) {

    // Secondary Constructor for use with new Tasks
    constructor(
        title: String = "",
        complete: Boolean = false,
        parentId: Long
    ) : this(null, title, complete, parentId)

    fun copySubtask(): SubtaskEntity {
        return SubtaskEntity(
            title, isComplete, parentId
        )
    }
}