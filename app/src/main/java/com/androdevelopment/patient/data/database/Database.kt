package com.androdevelopment.patient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.androdevelopment.patient.data.models.Result
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Database(
    entities = [Result::class], version = 1
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract val resultsDao: ResultsDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): String {
        return date.toString() ?: ""
    }
}