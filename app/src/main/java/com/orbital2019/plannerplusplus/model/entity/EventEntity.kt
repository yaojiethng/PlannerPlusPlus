package com.orbital2019.plannerplusplus.model.entity

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable, Taggable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    ) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    constructor(
        id: Int,
        title: String = "",
        dateTime: String = LocalDateTime.now().toString(),
        details: String? = "",
        repeated: Boolean = false,
        followUp: Boolean = false,
        tags: String = ""
    ) : this(title, dateTime, details, repeated, followUp, tags) {
        this.id = id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(dateTime)
        parcel.writeString(details)
        parcel.writeByte(if (repeated) 1 else 0)
        parcel.writeByte(if (followUp) 1 else 0)
        parcel.writeString(tags)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventEntity> {
        override fun createFromParcel(parcel: Parcel): EventEntity {
            return EventEntity(parcel)
        }

        override fun newArray(size: Int): Array<EventEntity?> {
            return arrayOfNulls(size)
        }
    }
}
