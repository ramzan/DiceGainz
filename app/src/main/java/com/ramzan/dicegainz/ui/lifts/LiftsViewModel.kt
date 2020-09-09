package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabaseDao
import kotlinx.coroutines.launch

class LiftsViewModel(
    val database: LiftDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val lifts = database.getAllLifts()

    init {
        for (i in 1..20) {
            val lift = Lift(i.toString(), i % 2)
            addLift(lift)
        }
    }

    fun addLift(lift: Lift) {
        Log.d("addLift", "Adding lift")
        viewModelScope.launch {
            if (hasItem(lift.name)) {
                Log.d("addLift", "Lift already exists!")
            } else {
                insert(lift)
                Log.d("addLift", "Lift added")
            }
        }
    }

    fun deleteLift(name: String) {
        Log.d("deleteLift", "Deleting lift $name")
        viewModelScope.launch {
            val lift = get(name)
            if (lift === null) {
                Log.d("deleteLift", "Lift $name does not exist!")
            } else {
                delete(lift)
                Log.d("deleteLift", "Lift $name deleted!")
            }
        }
    }

    private suspend fun insert(lift: Lift) {
        database.insert(lift)
    }

    private suspend fun get(name: String): Lift? {
        return database.get(name)
    }

    private suspend fun hasItem(name: String): Boolean {
        return database.hasItem(name)
    }

    private suspend fun delete(lift: Lift) {
        return database.delete(lift)
    }
}