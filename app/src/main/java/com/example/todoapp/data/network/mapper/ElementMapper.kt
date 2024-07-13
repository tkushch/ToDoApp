package com.example.todoapp.data.network.mapper


import android.os.Build
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.stringToImportance
import com.example.todoapp.data.network.dto.AddElementRequestDto
import com.example.todoapp.data.network.dto.ElementDto
import com.example.todoapp.data.network.dto.ListDto
import java.time.LocalDateTime
import java.time.ZoneOffset


object ElementMapper {
    fun fromDto(dto: ElementDto): TodoItem {
        return TodoItem(
            id = dto.id,
            text = dto.text,
            importance = stringToImportance(dto.importance),
            deadline = dto.deadline?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) },
            done = dto.done,
            creationDate = LocalDateTime.ofEpochSecond(dto.createdAt, 0, ZoneOffset.UTC),
            updatedDate = dto.changedAt.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
        )
    }

    fun toDto(item: TodoItem): ElementDto {
        return ElementDto(
            id = item.id,
            text = item.text,
            importance = item.importance.name.lowercase(),
            deadline = item.deadline?.toEpochSecond(ZoneOffset.UTC),
            done = item.done,
            color = "#FFFFFF",
            createdAt = item.creationDate.toEpochSecond(ZoneOffset.UTC),
            changedAt = item.updatedDate?.toEpochSecond(ZoneOffset.UTC)
                ?: item.creationDate.toEpochSecond(ZoneOffset.UTC),
            lastUpdatedBy = Build.ID

        )
    }

    fun toAddDto(item: TodoItem): AddElementRequestDto {
        return AddElementRequestDto(
            element = ElementDto(
                id = item.id,
                text = item.text,
                importance = item.importance.name.lowercase(),
                deadline = item.deadline?.toEpochSecond(ZoneOffset.UTC),
                done = item.done,
                color = "#FFFFFF",
                createdAt = item.creationDate.toEpochSecond(ZoneOffset.UTC),
                changedAt = item.updatedDate?.toEpochSecond(ZoneOffset.UTC)
                    ?: item.creationDate.toEpochSecond(ZoneOffset.UTC),
                lastUpdatedBy = Build.ID
            )
        )
    }

    fun toListDto(items: List<ElementDto>, revision: Int): ListDto {
        return  ListDto(
            status = "ok",
            list = items,
            revision = revision
        )
    }

}