package com.androdevelopment.patient.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "reportId") val reportId: Int,
    @ColumnInfo(name = "labName") val labName: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    val isEdited:Boolean,
    val description:String,
    val technicianComment:String,
    val pdfURL: String
)
