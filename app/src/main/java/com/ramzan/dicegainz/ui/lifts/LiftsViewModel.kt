package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.repository.Repository

class LiftsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

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

}