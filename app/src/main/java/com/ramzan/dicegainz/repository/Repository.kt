package com.ramzan.dicegainz.repository

import androidx.lifecycle.LiveData
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(database: LiftDatabase) {

    private val db = database.liftDatabaseDao

    val allTagsList = getAllTags()

    fun getAllLifts(): LiveData<List<Lift>> {
        return db.getAllLifts()
    }

    fun getLiftsForTag(tag: String): LiveData<List<Lift>> {
        return db.getLiftsForTag(tag)
    }

    private fun getAllTags(): LiveData<List<String>> {
        return db.getAllTagNames()
    }

    fun getTagNamesForLift(id: Long): LiveData<List<String>> {
        return db.getTagNamesForLift(id)
    }

    fun addLift(lift: Lift, tags: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = insert(lift)
            insertAll(tags.map { Tag(it, id) })
        }
    }


    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        CoroutineScope(Dispatchers.IO).launch {
            update(lift)
            insertAll(newTags)
            deleteAll(deletedTags)
        }
    }

    fun deleteLift(lift: Lift) {
        CoroutineScope(Dispatchers.IO).launch {
            delete(lift)
        }
    }


    private suspend fun insert(lift: Lift): Long {
        return db.insert(lift)
    }

    private suspend fun insertAll(tags: List<Tag>) {
        db.insertTags(tags)
    }

    private suspend fun deleteAll(tags: List<Tag>) {
        db.deleteAll(tags)
    }

    private suspend fun update(lift: Lift) {
        db.update(lift)
    }

    private suspend fun delete(lift: Lift) {
        return db.delete(lift)
    }
}