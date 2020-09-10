package com.ramzan.dicegainz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lift_table")
data class Lift(

    @PrimaryKey
    var name: String,

    @ColumnInfo(name = "tier")
    var tier: Int = BOTH,
)
