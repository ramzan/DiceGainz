package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.repository.Repository

class LiftsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

    val lifts = repo.getAllLifts()

}