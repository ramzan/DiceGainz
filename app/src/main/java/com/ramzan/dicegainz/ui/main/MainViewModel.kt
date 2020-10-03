package com.ramzan.dicegainz.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.T1
import com.ramzan.dicegainz.database.T2
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

    private val combinedValues =
        MediatorLiveData<Triple<List<Lift>?, List<Lift>?, List<Lift>?>>().apply {
            addSource(lifts1) {
                value = Triple(it, lifts2.value, lifts3.value)
            }
            addSource(lifts2) {
                value = Triple(lifts1.value, it, lifts3.value)
            }
            addSource(lifts3) {
                value = Triple(lifts1.value, lifts2.value, it)
            }
        }

    val liftsLoaded = Transformations.map(combinedValues) { triple ->
        triple.first != null && triple.second != null && triple.third != null
    }

    private var _lift1Text = MutableLiveData("")
    private var _lift2Text = MutableLiveData("")
    private var _lift3Text = MutableLiveData("")

    val lift1Text: MutableLiveData<String>
        get() = _lift1Text
    val lift2Text: MutableLiveData<String>
        get() = _lift2Text
    val lift3Text: MutableLiveData<String>
        get() = _lift3Text


    fun getAllLifts(liftNumber: Int) {
        when (liftNumber) {
            1 -> lifts1 = repo.getAllLifts()
            2 -> lifts2 = repo.getAllLifts()
            3 -> lifts3 = repo.getAllLifts()
        }
    }

    fun filterLifts(liftNumber: Int, tag: String) {
        when (liftNumber) {
            1 -> lifts1 = repo.getLiftsForTag(tag)
            2 -> lifts2 = repo.getLiftsForTag(tag)
            3 -> lifts3 = repo.getLiftsForTag(tag)
        }
    }

    fun roll(liftNumber: Int) {
        when (liftNumber) {
            1 -> updateLiftText(lifts1, _lift1Text)
            2 -> updateLiftText(lifts2, _lift2Text)
            3 -> updateLiftText(lifts3, _lift3Text)
        }
    }

    fun rollAll() {
        roll(1)
        roll(2)
        roll(3)
    }

    private fun updateLiftText(lifts: LiveData<List<Lift>>, liftText: MutableLiveData<String>) {
        val lift = lifts.value!!.random()
        liftText.value = "${lift.name} ${getRM(lift.tier)}RM"
    }

    private fun getRM(tier: Int): Int {
        return when (tier) {
            T1 -> (3..6).random()
            T2 -> (6..10).random()
            else -> (3..10).random()
        }
    }
}