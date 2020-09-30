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

    fun addLift(lift: Lift) {
        Log.d("addLift", "Adding lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch {
//            val id = insert(lift)
            insert(lift)
            Log.d("addLift", "Lift ${lift.name} added")
        }
    }


    fun updateLift(lift: Lift) {
        Log.d("updateLift", "Updating lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch {
            update(lift)
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

    private suspend fun insert(tag: Tag) {
        db.insert(tag)
    }

    private suspend fun update(lift: Lift) {
        db.update(lift)
    }

    private suspend fun delete(lift: Lift) {
        return db.delete(lift)
    }
}