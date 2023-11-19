package com.androdevelopment.patient.data.repositories.home

import com.androdevelopment.patient.data.database.ResultsDao
import com.androdevelopment.patient.data.models.Result
import kotlinx.coroutines.flow.Flow

class MainRepositoryImpl(private val dao: ResultsDao): MainRepository {

    override suspend fun getResults(): Flow<List<Result>> {
        return dao.getResults()
    }

    override suspend fun getResult(resultId: Int): Result {
        return dao.getResult(resultId)
    }

    override suspend fun insertResult(result: Result) {
        return dao.insertResult(result)
    }

    override suspend fun deleteResult(result: Result) {
        return dao.deleteResult(result)
    }


    override  fun getFirstFourOrderedByDate(): Flow<List<Result>> {
        return dao.getFirstFourOrderedByDate()
    }


}