package com.ramzan.dicegainz.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.T1
import com.ramzan.dicegainz.database.T2
import com.ramzan.dicegainz.repository.Repository

private const val ALL = "All"
const val LIFTS_FILTER_ID = 0
const val ROLL_FILTER1_ID = 1
const val ROLL_FILTER2_ID = 2
const val ROLL_FILTER3_ID = 3


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

    // ------------------------Filter methods and data------------------------
    private val tags = repo.allTagsList

    val tagList = Transformations.map(tags) {
        listOf(ALL) + it
    }

    fun updateFilterText(liftNumber: Int, tag: String) {
        when (liftNumber) {
            0 -> liftsFilterText.value = tag
            1 -> lift1FilterText.value = tag
            2 -> lift2FilterText.value = tag
            3 -> lift3FilterText.value = tag
        }
    }

    private fun getLifts(tag: String): LiveData<List<Lift>> {
        return when (tag) {
            ALL -> repo.getAllLifts()
            else -> repo.getLiftsForTag(tag)
        }
    }

    // -------------------------Lifts data----------------------------
    // Current tag selection in the filter of the Lifts tab
    private var liftsFilterText = MutableLiveData(ALL)

    // Lists of lifts to display
    private val _lifts = Transformations.switchMap(liftsFilterText) { tag -> getLifts(tag) }

    val lifts: LiveData<List<Lift>>
        get() = _lifts

    fun addLift(lift: Lift, tags: List<String>) {
        repo.addLift(lift, tags)
    }

    // ----------------------Roll data-----------------------------
    // Current tag selection in the filters in th Roll tab
    private var lift1FilterText = MutableLiveData(ALL)
    private var lift2FilterText = MutableLiveData(ALL)
    private var lift3FilterText = MutableLiveData(ALL)

    // Lists of lifts to roll from
    private val lifts1 = Transformations.switchMap(lift1FilterText) { tag -> getLifts(tag) }
    private val lifts2 = Transformations.switchMap(lift2FilterText) { tag -> getLifts(tag) }
    private val lifts3 = Transformations.switchMap(lift3FilterText) { tag -> getLifts(tag) }

    // Make sure lifts are loaded before rolling
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
        !triple.first.isNullOrEmpty() && !triple.second.isNullOrEmpty() && !triple.third.isNullOrEmpty()
    }

    // String containing the rolled lift
    private var _lift1Text = MutableLiveData("")
    private var _lift2Text = MutableLiveData("")
    private var _lift3Text = MutableLiveData("")

    val lift1Text: MutableLiveData<String>
        get() = _lift1Text
    val lift2Text: MutableLiveData<String>
        get() = _lift2Text
    val lift3Text: MutableLiveData<String>
        get() = _lift3Text

    private fun updateLiftText(lifts: LiveData<List<Lift>>, liftText: MutableLiveData<String>) {
        val lift = lifts.value!!.random()
        liftText.value = "${lift.name} ${getRM(lift.tier)}RM"
    }

    // Roll methods
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

    private fun getRM(tier: Int): Int {
        return when (tier) {
            T1 -> (3..6).random()
            T2 -> (6..10).random()
            else -> (3..10).random()
        }
    }
}