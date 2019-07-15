package com.orbital2019.plannerplusplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

// todo: decide on database parameters
// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "task_table")
data class TaskEntity(
    var title: String = "",
    var details: String? = "",
    var autoNumber: Boolean = false,
    var tags: String = ""
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(
        id: Int,
        title: String = "",
        details: String? = "",
        autoNumber: Boolean = false,
        tags: String = ""
    ) : this(title, details, autoNumber, tags) {
        this.id = id
    }
}
