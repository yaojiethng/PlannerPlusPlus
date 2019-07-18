package com.orbital2019.plannerplusplus.model.entity

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable, Taggable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    // Secondary Constructor for use with new Tasks
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

    // Secondary Constructor for use with Parcel
    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Int,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(details)
        parcel.writeByte(if (autoNumber) 1 else 0)
        parcel.writeString(tags)
        parcel.writeByte(if (complete) 1 else 0)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskEntity> {
        override fun createFromParcel(parcel: Parcel): TaskEntity {
            return TaskEntity(parcel)
        }

        override fun newArray(size: Int): Array<TaskEntity?> {
            return arrayOfNulls(size)
        }
    }
}
