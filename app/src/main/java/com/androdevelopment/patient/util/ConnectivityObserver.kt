package com.androdevelopment.patient.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        InternetAvailable, InternetUnavailable, Losing, InternetLost
    }
}