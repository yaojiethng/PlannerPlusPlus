package com.orbital2019.plannerplusplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "event_table")
data class EventEntity(
    var title: String = "",
    var dateTime: String = LocalDateTime.now().toString(),
    var details: String? = "",
    var repeated: Boolean = false,
    var followUp: Boolean = false,
    var tags: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
