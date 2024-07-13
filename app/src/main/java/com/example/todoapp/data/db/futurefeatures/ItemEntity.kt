package com.example.todoapp.data.db.futurefeatures

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.model.Importance
import java.time.LocalDateTime


/**
 * ItemEntity - новая модель для будущего ДЗ (аудио)
 */
@Entity(tableName = "todo_items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val text: String?,
    val audioFilePath: String?,
    val importance: String,
    val deadline: LocalDateTime?,
    val done: Boolean,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime?
)

fun ItemEntity.toDomainModel() = Item(
    id = id,
    text = text,
    audioFilePath = audioFilePath,
    importance = Importance.valueOf(importance),
    deadline = deadline,
    done = done,
    creationDate = creationDate,
    updatedDate = updatedDate
)

fun Item.toEntity() = ItemEntity(
    id = id,
    text = text,
    audioFilePath = audioFilePath,
    importance = importance.name,
    deadline = deadline,
    done = done,
    creationDate = creationDate,
    updatedDate = updatedDate
)
