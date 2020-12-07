package com.nazmar.dicegainz.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "lift_table")
data class Lift(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "tier")
    var tier: Int = BOTH,
) : Parcelable {
    constructor(name: String, tier: Int) : this(0, name, tier)
}
