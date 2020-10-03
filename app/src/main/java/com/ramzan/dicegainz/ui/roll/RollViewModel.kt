package com.ramzan.dicegainz.ui.roll

import android.app.Application
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.repository.Repository

class RollViewModel(application: Application) : ViewModel() {

    private val repo = Repository(LiftDatabase.getInstance(application))

    private var lifts1 = repo.getAllLifts()
    private var lifts2 = repo.getAllLifts()
    private var lifts3 = repo.getAllLifts()

    fun getAllLifts(liftNumber: Int) {
        when(liftNumber) {
            1 -> lifts1 = repo.getAllLifts()
            2 -> lifts2 = repo.getAllLifts()
            3 -> lifts1 = repo.getAllLifts()
        }
    }

    fun filterLifts(liftNumber: Int, tag: String) {
        when(liftNumber) {
            1 -> lifts1 = repo.getLiftsForTag(tag)
            2 -> lifts2 = repo.getLiftsForTag(tag)
            3 -> lifts1 = repo.getLiftsForTag(tag)
        }
    }

    private val tags = repo.allTagsList

    val tagList = Transformations.map(tags) {
        listOf("All") + it
    }
}