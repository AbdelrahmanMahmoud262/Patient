package com.androdevelopment.patient.di

import android.app.Application
import androidx.room.Room
import com.androdevelopment.patient.data.database.Database
import com.androdevelopment.patient.data.repositories.home.MainRepository
import com.androdevelopment.patient.data.repositories.home.MainRepositoryImpl
import com.androdevelopment.patient.data.repositories.results.ResultsRepository
import com.androdevelopment.patient.data.repositories.results.ResultsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Room.databaseBuilder(app, Database::class.java, "quran").build()
    }

    @Provides
    @Singleton
    fun provideMainRepository(db: Database): MainRepository {
        return MainRepositoryImpl(db.resultsDao)
    }

    @Provides
    @Singleton
    fun provideResultsRepository(db: Database): ResultsRepository {
        return ResultsRepositoryImpl(db.resultsDao)
    }
}