package com.example.todoapp.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
Converters - класс для преобразования типов для Room
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}