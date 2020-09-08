package com.ramzan.dicegainz.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Lift class with Room.
 */
@Dao
interface LiftDatabaseDao {
    @Insert
    suspend fun insert(lift: Lift)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param lift new value to write
     */
    @Update
    suspend fun update(lift: Lift)

    /**
     * Selects and returns the row that matches the supplied key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from lift_table WHERE name = :key")
    suspend fun get(key: String): Lift?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM lift_table")
    suspend fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by name in descending order.
     */
    @Query("SELECT * FROM lift_table ORDER BY name DESC")
    fun getAllLifts(): LiveData<List<Lift>>
}
