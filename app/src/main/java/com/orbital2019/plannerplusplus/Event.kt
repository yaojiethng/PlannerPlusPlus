package com.orbital2019.plannerplusplus

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "event_table")
data class Event(


    var title: String,
    var dateTime: LocalDateTime,
    @Ignore var description: String?,
    @Ignore var repeated: Boolean,
    @Ignore var followUp: Boolean,
    var tags: MutableCollection<String>
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}

/*{
    constructor() : this(
        null,
        "",
        LocalDateTime.now(),
        null,
        repeated = false,
        followUp = false,
        tags = MutableCollection<String>()
    )
}*/
