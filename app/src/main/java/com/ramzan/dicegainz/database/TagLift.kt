package com.ramzan.dicegainz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Lift::class,
        parentColumns = ["id"],
        childColumns = ["liftId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Tag::class,
        parentColumns = ["name"],
        childColumns = ["tagName"],
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "tag_lift_table",
    primaryKeys = ["tagName", "liftId"]
)
data class TagLift(
    @ColumnInfo(name = "tagName")
    val name: String,

    @ColumnInfo(name = "liftId")
    var liftId: Int
)
