package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabaseDao
import com.ramzan.dicegainz.database.T1
import com.ramzan.dicegainz.database.T2
import kotlinx.coroutines.launch

class LiftsViewModel(
    val database: LiftDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val lifts = database.getAllLifts()

}