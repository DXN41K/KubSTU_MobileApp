package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "works",
    foreignKeys = [
        ForeignKey(entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class WorkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "student_id")
    val student_id: Int,

    @ColumnInfo(name = "ugod")
    val ugod: Int,

    @ColumnInfo(name = "semestr")
    val semestr: Int,

    @ColumnInfo(name = "tipkontr_id")
    val tipkontr_id: Int,

    @ColumnInfo(name = "tipkontr_name")
    val tipkontr_name: String,

    @ColumnInfo(name = "grade")
    val oc_f: Int,

    @ColumnInfo(name = "work_name")
    val work_name: String,

    @ColumnInfo(name = "filename")
    val filename: String,

    @ColumnInfo(name = "file_id")
    val file_id: Int
)