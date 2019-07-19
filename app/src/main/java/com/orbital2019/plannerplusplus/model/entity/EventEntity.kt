/**
 * Entity representing one Event on the calendar.
 * todo: https://www.sqlite.org/foreignkeys.html
 * todo: https://medium.com/androiddevelopers/room-time-2b4cf9672b98
 * todo: https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
 */
package com.orbital2019.plannerplusplus.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

/**
 * Entity representing one Event on the calendar
 * todo: how to represent and show repeated events?
 * @param eventStartTime The time at which the event starts, stored as a String
 *  (automatically converted from OffsetDateTime)
 * @param tags All tags that are associated with the EventEntity, truncated into a single string
 */
@Entity(tableName = "event_table")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String = "",
    var eventStartTime: OffsetDateTime = OffsetDateTime.now(),
    var eventDuration: OffsetDateTime? = eventStartTime,
    //todo: implement duration as an end time, within the same day as start time
    var details: String? = "",
    var repeated: Boolean = false,
    var followUp: Boolean = false,
    var tags: String = ""
) : Parcelable, Taggable {



    constructor(parcel: Parcel) : this(
        id = parcel.readSerializable() as Long,
        title = parcel.readString()!!,
        eventStartTime = TiviTypeConverters.toOffsetDateTime(parcel.readString())!!,
        eventDuration = TiviTypeConverters.toOffsetDateTime(parcel.readString())!!,
        details = parcel.readString(),
        repeated = parcel.readByte() != 0.toByte(),
        followUp = parcel.readByte() != 0.toByte(),
        tags = parcel.readString()!!
    )

    constructor(
        title: String = "",
        eventStartTime: OffsetDateTime,
        eventDuration: OffsetDateTime,
        details: String? = "",
        repeated: Boolean = false,
        followUp: Boolean = false,
        tags: String = ""
    ) : this(null, title, eventStartTime, eventDuration, details, repeated, followUp, tags)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(title)
        parcel.writeString(TiviTypeConverters.fromOffsetDateTime(eventStartTime))
        parcel.writeString(TiviTypeConverters.fromOffsetDateTime(eventDuration))
        parcel.writeString(details)
        parcel.writeByte(if (repeated) 1 else 0)
        parcel.writeByte(if (followUp) 1 else 0)
        parcel.writeString(tags)
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

    // Type Converters from OffsetDateTime to String
    object TiviTypeConverters {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): OffsetDateTime? {
            return value?.let {
                return formatter.parse(value, OffsetDateTime::from)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
            return date?.format(formatter)
        }
    }


}
