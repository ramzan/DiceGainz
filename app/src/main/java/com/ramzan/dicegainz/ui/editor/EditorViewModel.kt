package com.ramzan.dicegainz.ui.editor

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabaseDao
import kotlinx.coroutines.launch

class EditorViewModel(val database: LiftDatabaseDao, application: Application, lift: Lift?) : AndroidViewModel(application){

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

    fun deleteLift(lift: Lift) {
        viewModelScope.launch {
            delete(lift)
            Log.d("deleteLift", "Lift ${lift.name} deleted!")
        }
    }

    private suspend fun insert(lift: Lift) {
        database.insert(lift)
    }


    private suspend fun hasItem(name: String): Boolean {
        return name == "1"
    }

    private suspend fun delete(lift: Lift) {
        return database.delete(lift)
    }

}