package com.example.kubstu_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "student",
    foreignKeys = [
        ForeignKey(
            entity = FormaObEntity::class,
            parentColumns = ["id"],
            childColumns = ["formaob_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = SpecsEntity::class,
            parentColumns = ["id"],
            childColumns = ["spec_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class StudentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "imja")
    val imja: String,

    @ColumnInfo(name = "fam")
    val fam: String,

    @ColumnInfo(name = "otch")
    val otch: String,

    @ColumnInfo(name = "gr")
    val gr: String,

    @ColumnInfo(name = "kurs")
    val kurs: Int,

    @ColumnInfo(name = "inst_id")
    val inst_id: Int,

    @ColumnInfo(name = "formaob_id")
    val formaob_id: Int,

    @ColumnInfo(name = "spec_id")
    val spec_id: Int
)