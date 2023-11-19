package com.androdevelopment.patient.data.repositories.results

import com.androdevelopment.patient.data.database.ResultsDao
import com.androdevelopment.patient.data.models.Result
import kotlinx.coroutines.flow.Flow

class ResultsRepositoryImpl(private val dao: ResultsDao) : ResultsRepository {
    override fun getResults(): Flow<List<Result>> {
        return dao.getResults()
    }

    override suspend fun getResult(resultId: Int): Result {
        return dao.getResult(resultId)
    }

    override suspend fun insertResult(result: Result) {
        dao.insertResult(result)
    }

    override suspend fun deleteResult(result: Result) {
        dao.deleteResult(result)
    }

    override fun getFirstFourOrderedByDate(): Flow<List<Result>> {
        return dao.getFirstFourOrderedByDate()
    }


}