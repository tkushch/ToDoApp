package com.example.todoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * TodoDatabase - класс для работы с БД
 */

@Database(entities = [TodoItemEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract val dao: TodoItemDao
}