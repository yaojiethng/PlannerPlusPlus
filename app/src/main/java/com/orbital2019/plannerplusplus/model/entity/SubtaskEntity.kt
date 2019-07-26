package com.orbital2019.plannerplusplus.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a subtask
 * set onDelete = CASCADE: if task will be deleted, also delete all of its subtasks.
 */
@Entity(
    indices = [Index("parentId")],
    tableName = "subtask_table",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = CASCADE
        )]
)
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