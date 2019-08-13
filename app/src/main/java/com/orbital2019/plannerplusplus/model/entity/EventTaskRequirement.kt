/**
 * A many-to-many relationship exists between two entities if for one entity instance there may be multiple records in
 * the other table, and vice versa. One Event can have multiple Tasks registered as requirements, and one Task can be
 * a requirement for multiple Events.
 * In order to implement this sort of relationship we need to introduce a third, cross-reference, table.
 */
package com.orbital2019.plannerplusplus.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * This table holds the relationship between the two entities, by having two FOREIGN KEYs,
 * each of which references the PRIMARY KEY of one of the tables for which we want to create this relationship.
 * @param event_id eventEntity.id (long) is mapped to taskRequirements.event_id
 * @param task_id taskEntity.id (long) is mapped to taskRequirements.task_id
 * primaryKey is [event, task] which is a unique array of two long values.
 */
@Entity(
    tableName = "task_requirement_by_event",
    indices = [
        Index("event_id", "task_id")
    ],
    primaryKeys = ["event_id", "task_id"],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
class EventTaskRequirement(
    var event_id: Long,
    var task_id: Long
)
