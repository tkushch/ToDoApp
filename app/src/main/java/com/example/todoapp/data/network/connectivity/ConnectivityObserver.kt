package com.example.todoapp.data.network.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    fun checkCurrentStatus(): Status

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

}