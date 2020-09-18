package com.ramzan.dicegainz.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "tag_table",
)
data class Tag(
    @PrimaryKey
    val name: String
)
