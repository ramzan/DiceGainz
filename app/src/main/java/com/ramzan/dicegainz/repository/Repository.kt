package com.ramzan.dicegainz.repository

import android.util.Log
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

    private fun getAllTags(): LiveData<List<String>> {
        return db.getAllTagNames()
    }

    fun getTagNamesForLift(id: Long): LiveData<List<String>> {
        return db.getTagNamesForLift(id)
    }

    fun addLift(lift: Lift, tags: List<String>) {
        Log.d("addLift", "Adding lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch {
            val id = insert(lift)
            insertAll(tags.map { Tag(it, id) })
            Log.d("addLift", "Lift ${lift.name} added")
        }
    }


    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        Log.d("updateLift", "Updating lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch {
            update(lift)
            insertAll(newTags)
            deleteAll(deletedTags)
            Log.d("updateLift", "Lift ${lift.name} updated")
        }
    }

    fun deleteLift(lift: Lift) {
        Log.d("deleteLift", "Deleting lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch {
            delete(lift)
            Log.d("deleteLift", "Lift ${lift.name} deleted!")
        }
    }


    private suspend fun insert(lift: Lift): Long {
        return db.insert(lift)
    }

    private suspend fun insertAll(tags: List<Tag>) {
        db.insertAll(tags)
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