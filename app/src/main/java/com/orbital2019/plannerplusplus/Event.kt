package com.orbital2019.plannerplusplus

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "event_table")
data class Event public constructor(
    var title: String = "",
    @Ignore var dateTimeRaw: LocalDateTime = LocalDateTime.now(),
    @Ignore var description: String? = null,
    @Ignore var repeated: Boolean = false,
    @Ignore var followUp: Boolean = false,
    @Ignore var tags: MutableCollection<String> = mutableListOf()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var dateTime: String = dateTimeRaw.toString()
}
