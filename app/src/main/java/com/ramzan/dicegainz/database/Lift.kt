package com.ramzan.dicegainz.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "lift_table")
data class Lift(

    @PrimaryKey
    var name: String,

    @ColumnInfo(name = "tier")
    var tier: Int = BOTH,
) : Parcelable
