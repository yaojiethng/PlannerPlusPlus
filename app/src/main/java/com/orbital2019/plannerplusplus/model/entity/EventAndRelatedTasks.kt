/**
 * A convenience annotation which can be used in a Pojo to automatically fetch relation entities.
 * When the Pojo is returned from a query, all of its relations are also fetched by Room.
 * Note that @Relation annotation can be used only in Pojo classes, an Entity class cannot have relations.
 */
package com.orbital2019.plannerplusplus.model.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room Relation providing a mapping between an Event and its linked TaskEntities
 */

class EventAndRelatedTasks(

    @Embedded
    val event: EventEntity,

    /**
     * The type of the field annotated with Relation must be a List or Set.
     * The Entity type for this class is TaskEntity.
     * If you would like to specify which columns are fetched from the child Entity, you can use projection()
     */
    @Relation(parentColumn = "id", entityColumn = "id", entity = TaskEntity::class)
    val tasks: Set<TaskEntity>
)

