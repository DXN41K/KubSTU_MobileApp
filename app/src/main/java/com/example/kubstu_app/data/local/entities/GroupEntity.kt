package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "groups",
    foreignKeys = [
        ForeignKey(entity = InstituteEntity::class,
            parentColumns = ["id"],
            childColumns = ["inst_id"],
            onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = FormaObEntity::class,
            parentColumns = ["id"],
            childColumns = ["formaob_id"],
            onDelete = ForeignKey.RESTRICT)
    ]
)
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "inst_id")
    val inst_id: Int,

    @ColumnInfo(name = "formaob_id")
    val formaob_id: Int,

    @ColumnInfo(name = "kurs")
    val kurs: Int
)
