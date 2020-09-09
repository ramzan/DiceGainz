package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ramzan.dicegainz.database.LiftDatabaseDao

class LiftsViewModel(
    val database: LiftDatabaseDao,
    application: Application) : AndroidViewModel(application) {
    val lifts = database.getAllLifts()
}
