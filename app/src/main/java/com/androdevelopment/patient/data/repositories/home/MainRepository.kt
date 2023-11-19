package com.androdevelopment.patient.data.repositories.home

import com.androdevelopment.patient.data.models.Result
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getResults(): Flow<List<Result>>

    suspend fun getResult(resultId: Int): Result

    suspend fun insertResult(result: Result)

    suspend fun deleteResult(result: Result)


    fun getFirstFourOrderedByDate(): Flow<List<Result>>
}