package com.nazmar.dicegainz.database

import androidx.lifecycle.LiveData
import androidx.room.*


interface BaseDao<T> {
    @Insert
    suspend fun insert(t: T): Long

    @Insert
    suspend fun insertAll(t: List<T>)

    @Update
    suspend fun update(t: T)

    @Delete
    suspend fun delete(t: T)

    @Delete
    suspend fun deleteAll(t: List<T>)
}

@Dao
interface LiftDao : BaseDao<Lift> {

    @Query("SELECT * from lift_table WHERE id = :key")
    suspend fun getLift(key: Long): Lift?

    @Query("SELECT * FROM lift_table ORDER BY name COLLATE NOCASE ASC")
    fun getAllLifts(): LiveData<List<Lift>>

    @Query("SELECT * FROM lift_table ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllLiftsOneShot(): List<Lift>

    @Query("SELECT * FROM lift_table ORDER BY name ASC")
    fun getAllLiftsTest(): List<Lift>

    @Query(
        """SELECT lift_table.*
            FROM lift_table
            JOIN tag_table
            ON tag_table.liftId = lift_table.id
            WHERE tag_table.tagName = :tagName
            ORDER BY name ASC"""
    )
    fun getLiftsForTagTest(tagName: String): List<Lift>

    @Query(
        """SELECT lift_table.*
            FROM lift_table
            JOIN tag_table
            ON tag_table.liftId = lift_table.id
            WHERE tag_table.tagName = :tagName
            ORDER BY name COLLATE NOCASE ASC"""
    )
    fun getLiftsForTag(tagName: String): LiveData<List<Lift>>

    @Query(
        """SELECT lift_table.*
            FROM lift_table
            JOIN tag_table
            ON tag_table.liftId = lift_table.id
            WHERE tag_table.tagName = :tagName
            ORDER BY name COLLATE NOCASE ASC"""
    )
    suspend fun getLiftsForTagOneShot(tagName: String): List<Lift>

}

@Dao
interface TagDao : BaseDao<Tag> {

    @Query("SELECT * from tag_table WHERE tagName = :tagName AND liftId = :liftId")
    suspend fun getTag(tagName: String, liftId: Long): Tag?

    @Query("SELECT DISTINCT tagName FROM tag_table ORDER BY tagName ASC")
    fun getAllTagNamesTest(): List<String>

    @Query("SELECT DISTINCT tagName FROM tag_table ORDER BY tagName COLLATE NOCASE ASC")
    fun getAllTagNames(): LiveData<List<String>>

    @Query(
        """SELECT DISTINCT(tagName) as tag_names
            FROM tag_table 
            WHERE liftId = :liftId
            ORDER BY tagName ASC"""
    )
    fun getTagNamesForLiftTest(liftId: Long): List<String>

    @Query(
        """SELECT DISTINCT(tagName) as tag_names
            FROM tag_table
            WHERE liftId = :liftId
            ORDER BY tagName COLLATE NOCASE ASC"""
    )
    suspend fun getTagNamesForLift(liftId: Long): List<String>
}