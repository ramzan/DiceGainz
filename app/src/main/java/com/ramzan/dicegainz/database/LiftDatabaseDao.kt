package com.ramzan.dicegainz.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the Lift class with Room.
 */
@Dao
interface LiftDatabaseDao {
    @Insert
    suspend fun insert(lift: Lift)

    @Update
    suspend fun update(lift: Lift)

    @Query("SELECT * from lift_table WHERE id = :key")
    suspend fun get(key: Int): Lift?

    @Delete
    suspend fun delete(lift: Lift)

    @Query("SELECT * FROM lift_table ORDER BY name ASC")
    fun getAllLifts(): LiveData<List<Lift>>
}
