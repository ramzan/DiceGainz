package com.ramzan.dicegainz.ui.roll

import android.app.Application
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.repository.Repository

class RollViewModel(application: Application) : ViewModel() {

    private val repo = Repository(LiftDatabase.getInstance(application))

    private val tags = repo.allTagsList

    val tagList = Transformations.map(tags) {
        listOf("All") + it
    }
}