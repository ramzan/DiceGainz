package com.nazmar.dicegainz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lift_table")
data class Lift(

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "tier")
    var tier: Int = BOTH,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)