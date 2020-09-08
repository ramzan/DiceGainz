package com.ramzan.dicegainz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 Tiers:
 0: Both T1 and T2
 1: T1
 2: T2
 */
@Entity(tableName = "lift_table")
data class Lift(
//    @PrimaryKey(autoGenerate = true)
//    var liftId: Long = 0L,

//    @ColumnInfo(name = "name")
    @PrimaryKey
    val name: String = "",

    @ColumnInfo(name = "tier")
    var tier: Int = 0,
)
