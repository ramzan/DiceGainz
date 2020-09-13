package com.ramzan.dicegainz.ui.roll

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ramzan.dicegainz.database.LiftDatabaseDao

class RollViewModel(
    val database: LiftDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val lifts = database.getAllLifts()

}