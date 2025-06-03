package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "grades",
    foreignKeys = [
        ForeignKey(entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class GradesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "student_id")
    val student_id: Int,

    @ColumnInfo(name = "disc_name")
    val disc_name: String,

    @ColumnInfo(name = "tipkontr_id")
    val tipkontr_id: Int,

    @ColumnInfo(name = "tipkontr_lname")
    val tipkontr_lname: String,

    @ColumnInfo(name = "oc_f")
    val oc_f: Int,

    @ColumnInfo(name = "work_name")
    val work_name: String?,

    @ColumnInfo(name = "filename")
    val filename: String?,

    @ColumnInfo(name = "file_id")
    val file_id: Int?,

    @ColumnInfo(name = "title")
    val title: String,
)
