package com.ramzan.dicegainz.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the Lift class with Room.
 */
@Dao
interface LiftDatabaseDao {

    /*
     * Lift methods
     */
    @Insert
    suspend fun insert(lift: Lift): Long

    @Update
    suspend fun update(lift: Lift)

    @Query("SELECT * from lift_table WHERE id = :key")
    suspend fun getLift(key: Long): Lift?

    @Delete
    suspend fun delete(lift: Lift)

    @Query("SELECT * FROM lift_table ORDER BY name ASC")
    fun getAllLifts(): LiveData<List<Lift>>

    @Query("SELECT * FROM lift_table ORDER BY id ASC")
    fun getAllLiftsTest(): List<Lift>

    /*
     * Tag methods
     */
    @Insert
    suspend fun insert(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)

    @Query("SELECT * from tag_table WHERE tagName = :tagName AND liftId = :liftId")
    suspend fun getTag(tagName: String, liftId: Long): Tag?

    @Query("SELECT DISTINCT tagName FROM tag_table ORDER BY tagName ASC")
    fun getAllTagsTest(): List<String>

    @Query("SELECT DISTINCT tagName FROM tag_table ORDER BY tagName ASC")
    fun getAllTags(): LiveData<List<String>>

    /*
     * Complex methods
     */

    @Query(
        """SELECT DISTINCT(tagName) as tag_names
            FROM tag_table 
            WHERE liftId = :liftId"""
    )
    fun getTagNamesForLiftTest(liftId: Long): List<String>

    @Query(
        """SELECT DISTINCT(tagName) as tag_names
            FROM tag_table 
            WHERE liftId = :liftId"""
    )
    fun getTagNamesForLift(liftId: Long): LiveData<List<String>>

    @Query(
        """SELECT *
            FROM lift_table
            JOIN tag_table
            ON tag_table.liftId = lift_table.id
            WHERE tag_table.tagName = :tagName"""
    )
    fun getLiftsForTagTest(tagName: String): List<Lift>

    @Query(
        """SELECT *
            FROM lift_table
            JOIN tag_table
            ON tag_table.liftId = lift_table.id
            WHERE tag_table.tagName = :tagName"""
    )
    fun getLiftsForTag(tagName: String): LiveData<List<Lift>>
}
