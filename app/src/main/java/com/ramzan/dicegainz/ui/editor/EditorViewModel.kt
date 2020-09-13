package com.ramzan.dicegainz.ui.editor

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabaseDao
import kotlinx.coroutines.launch

class EditorViewModel(val database: LiftDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    fun addLift(lift: Lift) {
        Log.d("addLift", "Adding lift ${lift.name}")
        viewModelScope.launch {
            insert(lift)
            Log.d("addLift", "Lift ${lift.name} added")
        }
    }

    fun updateLift(lift: Lift) {
        Log.d("updateLift", "Updating lift ${lift.name}")
        viewModelScope.launch {
            update(lift)
            Log.d("updateLift", "Lift ${lift.name} updated")
        }
    }

    fun deleteLift(lift: Lift) {
        Log.d("deleteLift", "Deleting lift ${lift.name}")
        viewModelScope.launch {
            delete(lift)
            Log.d("deleteLift", "Lift ${lift.name} deleted!")
        }
    }

    private suspend fun insert(lift: Lift) {
        database.insert(lift)
    }

    private suspend fun update(lift: Lift) {
        database.update(lift)
    }


    private suspend fun delete(lift: Lift) {
        return database.delete(lift)
    }

}