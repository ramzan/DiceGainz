package com.nazmar.dicegainz.repository

import androidx.lifecycle.LiveData
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.database.LiftDatabase
import com.nazmar.dicegainz.database.Tag
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
            val id = db.insert(lift)
            db.insertTags(tags.map { Tag(it, id) })
        }
    }


    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.update(lift)
            db.insertTags(newTags)
            db.deleteAll(deletedTags)
        }
    }

    fun deleteLift(lift: Lift) {
        CoroutineScope(Dispatchers.IO).launch {
            db.delete(lift)
        }
    }
}