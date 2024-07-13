package com.example.todoapp.data.db.futurefeatures


import com.example.todoapp.data.model.Importance
import java.time.LocalDateTime


/**
 * Item - новая модель для будущего ДЗ (аудио)
 */
data class Item(
    val id: String,
    val text: String?,
    val audioFilePath: String?,
    val importance: Importance,
    val deadline: LocalDateTime?,
    val done: Boolean,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime?
)


