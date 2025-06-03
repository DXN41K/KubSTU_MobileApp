package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(entity = DiscEntity::class,
            parentColumns = ["id"],
            childColumns = ["disc_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class StudentNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "disc_id")
    val disc_id: Int,

    @ColumnInfo(name = "content")
    val content: String
)