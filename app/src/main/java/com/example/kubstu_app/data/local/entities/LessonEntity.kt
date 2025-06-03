package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = KindOfNagrEntity::class,
            parentColumns = ["id"],
            childColumns = ["kindofnagr_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DiscEntity::class,
            parentColumns = ["id"],
            childColumns = ["disc_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("kindofnagr_id"),
        Index("disc_id")
    ]
)
class LessonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "pair")
    val pair: Int,

    @ColumnInfo(name = "classroom")
    val classroom: String,

    @ColumnInfo(name = "teacher")
    val teacher: String,


    @ColumnInfo(name = "kindofnagr_id")
    val kindofnagr_id: Int,

    @ColumnInfo(name = "disc_id")
    val disc_id: Int
)