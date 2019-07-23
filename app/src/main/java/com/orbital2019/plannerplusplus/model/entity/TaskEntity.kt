package com.orbital2019.plannerplusplus.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

// todo: decide on database parameters
// todo: add subtasks based on https://www.sqlite.org/foreignkeys.html
// additional modifiers:
// @Ignore to remove entries from the table
// @ColumnInfo(name = "") to set custom column names

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String = "",
    var details: String? = "",
    var autoNumber: Boolean = false,
    var tags: String = "",
    var isComplete: Boolean = false
) : Parcelable, Taggable {

    // Secondary Constructor for use with Parcel
    constructor(parcel: Parcel) : this(
        id = parcel.readSerializable() as Long?,
        title = parcel.readString()!!,
        details = parcel.readString(),
        autoNumber = parcel.readByte() != 0.toByte(),
        tags = parcel.readString()!!,
        isComplete = parcel.readByte() != 0.toByte()
    )

    // Secondary Constructor for use with new Tasks
    constructor(
        title: String = "",
        details: String?,
        autoNumber: Boolean = false,
        tags: String = "",
        complete: Boolean = false
    ) : this(null, title, details, autoNumber, tags, complete)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(title)
        parcel.writeString(details)
        parcel.writeByte(if (autoNumber) 1 else 0)
        parcel.writeString(tags)
        parcel.writeByte(if (isComplete) 1 else 0)
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

    fun copyTask(): TaskEntity {
        return TaskEntity(
            title, details, autoNumber, tags, isComplete
        )
    }
}
