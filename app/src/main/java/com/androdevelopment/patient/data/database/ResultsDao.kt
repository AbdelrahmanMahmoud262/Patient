package com.androdevelopment.patient.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androdevelopment.patient.data.models.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Delete
    suspend fun deleteResult(result: Result)

    @Query("select * from results")
    fun getResults(): Flow<List<Result>>

    @Query("select * from results where id = :resultId")
    suspend fun getResult(resultId: Int): Result

    @Query("select * from results order by date limit 4")
    fun getFirstFourOrderedByDate(): Flow<List<Result>>
}

