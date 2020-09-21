package com.ramzan.dicegainz.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val database: LiftDatabase) {

    private val db = database.liftDatabaseDao

    fun getAllLifts(): LiveData<List<Lift>> {
        return db.getAllLifts()
    }

    fun getAllTags(): LiveData<List<Tag>> {
        return db.getAllTags()
    }

    fun getTagNamesForLift(id: Int): LiveData<List<String>> {
        return db.getTagNamesForLift(id)
    }

    fun addLift(lift: Lift) {
        Log.d("addLift", "Adding lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch{
            insert(lift)
            Log.d("addLift", "Lift ${lift.name} added")
        }
    }

    //    suspend fun addTag(tag: Tag) {
//        Log.d("addLift", "Adding lift ${tag.name}")
//        CoroutineScope(Dispatchers.IO).launch{
//            insert(tag)
//            Log.d("addLift", "Lift ${tag.name} added")
//        }
//    }


    fun updateLift(lift: Lift) {
        Log.d("updateLift", "Updating lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch{
            update(lift)
            Log.d("updateLift", "Lift ${lift.name} updated")
        }
    }

    fun deleteLift(lift: Lift) {
        Log.d("deleteLift", "Deleting lift ${lift.name}")
        CoroutineScope(Dispatchers.IO).launch{
            delete(lift)
            Log.d("deleteLift", "Lift ${lift.name} deleted!")
        }
    }


    private suspend fun insert(lift: Lift) {
        db.insert(lift)
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