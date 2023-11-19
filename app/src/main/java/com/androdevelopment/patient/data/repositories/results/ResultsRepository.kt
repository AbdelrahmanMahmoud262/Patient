package com.androdevelopment.patient.data.repositories.results

import androidx.lifecycle.MutableLiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androdevelopment.patient.data.models.FilterModel
import com.androdevelopment.patient.data.models.Result
import kotlinx.coroutines.flow.Flow

interface ResultsRepository {

    fun getResults(): Flow<List<Result>>

    suspend fun getResult(resultId: Int): Result

    suspend fun insertResult(result: Result)

    suspend fun deleteResult(result: Result)


    fun getFirstFourOrderedByDate(): Flow<List<Result>>
}