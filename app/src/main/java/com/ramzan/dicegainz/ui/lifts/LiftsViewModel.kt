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
            var lift = Lift()
            lift.name = i.toString()
            lift.tier = i % 2
            addLift(lift)
        }
    }

    fun addLift(lift: Lift) {
        Log.d("addLift", "Adding lift")
        viewModelScope.launch {
            suspend { database.insert(lift) }()
        }
        Log.d("addLift", lifts.value.toString())
        Log.d("addLift", "Lift added")
    }
}