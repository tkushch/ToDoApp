/**
 * AddElementRequestDto - класс формата для сетевого запроса на добавление элемента
 */

package com.example.todoapp.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AddElementRequestDto(
    @SerializedName("element") val element: ElementDto,
)


