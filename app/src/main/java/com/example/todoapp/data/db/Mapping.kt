package com.example.todoapp.data.db

import com.example.todoapp.data.model.TodoItem

fun TodoItemEntity.toDomainModel() = TodoItem(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    done = done,
    creationDate = creationDate,
    updatedDate = updatedDate
)

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    done = done,
    creationDate = creationDate,
    updatedDate = updatedDate
)