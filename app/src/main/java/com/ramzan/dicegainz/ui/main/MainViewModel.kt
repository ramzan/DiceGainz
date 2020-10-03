package com.ramzan.dicegainz.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.repository.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

    // Lifts data
    private var _lifts = MutableLiveData(repo.getAllLifts())

    val lifts: LiveData<LiveData<List<Lift>>>
        get() = _lifts

    private val tags = repo.allTagsList

    val tagList = Transformations.map(tags) {
        listOf("All") + it
    }

    fun addLift(lift: Lift, tags: List<String>) {
        repo.addLift(lift, tags)
    }

    fun filterLifts(tag: String) {
        _lifts.value = if (tag == "All") {
            repo.getAllLifts()
        } else {
            repo.getLiftsForTag(tag)
        }
    }

    // Roll data
    private var lifts1 = repo.getAllLifts()
    private var lifts2 = repo.getAllLifts()
    private var lifts3 = repo.getAllLifts()

    fun getAllLifts(liftNumber: Int) {
        when (liftNumber) {
            1 -> lifts1 = repo.getAllLifts()
            2 -> lifts2 = repo.getAllLifts()
            3 -> lifts1 = repo.getAllLifts()
        }
    }

    fun filterLifts(liftNumber: Int, tag: String) {
        when (liftNumber) {
            1 -> lifts1 = repo.getLiftsForTag(tag)
            2 -> lifts2 = repo.getLiftsForTag(tag)
            3 -> lifts1 = repo.getLiftsForTag(tag)
        }
    }
}