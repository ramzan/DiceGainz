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
    suspend fun insert(lift: Lift)

    @Update
    suspend fun update(lift: Lift)

    @Query("SELECT * from lift_table WHERE id = :key")
    suspend fun getLift(key: Int): Lift?

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

    @Query("SELECT * from tag_table WHERE name = :name")
    suspend fun getTag(name: String): Tag?

    @Delete
    suspend fun delete(tag: Tag)

    @Query("SELECT * FROM tag_table ORDER BY name ASC")
    fun getAllTagsTest(): List<Tag>

    @Query("SELECT * FROM tag_table ORDER BY name ASC")
    fun getAllTags(): LiveData<List<Tag>>

    /*
     * TagLift methods
     */
    @Insert
    suspend fun insert(tagLift: TagLift)

    @Delete
    suspend fun delete(tagLift: TagLift)

    @Query("SELECT * from tag_lift_table WHERE tagName = :tagName AND liftId = :liftId")
    suspend fun getTagLift(tagName: String, liftId: Int): TagLift?

    /*
     * Complex methods
     */

    @Query("""SELECT DISTINCT(tagName) as tag_names
            FROM tag_lift_table 
            WHERE liftId = :liftId""")
    fun getTagNamesForLiftTest(liftId: Int): List<String>

    @Query("""SELECT DISTINCT(tagName) as tag_names
            FROM tag_lift_table 
            WHERE liftId = :liftId""")
    fun getTagNamesForLift(liftId: Int): LiveData<List<String>>

    @Query("""SELECT *
            FROM tag_lift_table JOIN lift_table
            ON tag_lift_table.liftId = lift_table.id
            WHERE tag_lift_table.tagName = :tagName""")
    fun getLiftsForTagTest(tagName: String): List<Lift>

    @Query("""SELECT *
            FROM tag_lift_table JOIN lift_table
            ON tag_lift_table.liftId = lift_table.id
            WHERE tag_lift_table.tagName = :tagName""")
    fun getLiftsForTag(tagName: String): LiveData<List<Lift>>
}
