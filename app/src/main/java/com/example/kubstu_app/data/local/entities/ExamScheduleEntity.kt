package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_schedule")
data class ExamScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val examScheduleId: Long = 0,

    @ColumnInfo(name = "disc_id")
    val disc_id: Int,

    @ColumnInfo(name = "disc_name")
    val disc_name: String,

    @ColumnInfo(name = "date_sd")
    val date_sd: String,

    @ColumnInfo(name = "time_sd")
    val time_sd: String,

    @ColumnInfo(name = "classroom")
    val classroom: String,

    @ColumnInfo(name = "teacher")
    val teacher: String
)