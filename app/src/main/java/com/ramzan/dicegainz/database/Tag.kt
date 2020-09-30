package com.ramzan.dicegainz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Lift::class,
            parentColumns = ["id"],
            childColumns = ["liftId"],
            onDelete = ForeignKey.CASCADE
        )],
    tableName = "tag_table",
    primaryKeys = ["tagName", "liftId"]
)
data class Tag(
    @ColumnInfo(name = "tagName")
    val name: String,

    @ColumnInfo(name = "liftId", index = true)
    var liftId: Long
)
