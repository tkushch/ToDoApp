package com.example.todoapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ListDto(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<ElementDto>,
    @SerializedName("revision") val revision: Int
)