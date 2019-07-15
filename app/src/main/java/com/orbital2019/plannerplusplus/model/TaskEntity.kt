package com.orbital2019.plannerplusplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// todo: decide on database parameters
// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "task_table")
data class TaskEntity(
    var title: String = "",
    var details: String? = "",
    var autoNumber: Boolean = false,
    var tags: String = "",
    var complete: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(
        id: Int,
        title: String = "",
        details: String? = "",
        autoNumber: Boolean = false,
        tags: String = "",
        complete: Boolean = false
    ) : this(title, details, autoNumber, tags, complete) {
        this.id = id
    }
}
