package com.nazmar.dicegainz.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.database.T1
import com.nazmar.dicegainz.database.T2
import com.nazmar.dicegainz.repository.Repository
import com.nazmar.dicegainz.ui.roll.RollCard
import com.nazmar.dicegainz.updateFilterText
import com.nazmar.dicegainz.updateRollResult
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _rollCards = MutableLiveData(
        mutableListOf<RollCard>().apply {
            for (i in 0..3) {
                this.add(RollCard(i))
            }
        }
    )

    val rollCards: LiveData<MutableList<RollCard>>
        get() = _rollCards

    val lifts = Repository.getAllLifts()

    val tags = Repository.allTagsList

    fun roll(index: Int) {
        viewModelScope.launch {
            rollCards.value?.get(index)?.let { card ->
                val lift = (tags.value?.let {
                    if (it.contains(card.filterText)) {
                        Repository.getLiftsForTagOneShot(card.filterText)
                    } else {
                        Repository.getAllLiftsOneShot()
                    }
                } ?: Repository.getAllLiftsOneShot()).random()
                _rollCards.updateRollResult(index, "${lift.name} ${getRM(lift.tier)}RM")
            }
        }
    }

    fun updateFilterText(position: Int, text: String) = _rollCards.updateFilterText(position, text)

    fun rollAll() {
        rollCards.value?.let { list ->
            if (!lifts.value.isNullOrEmpty()) list.indices.forEach { roll(it) }
        }
    }

    private fun getRM(tier: Int): Int {
        return when (tier) {
            T1 -> (3..6).random()
            T2 -> (6..10).random()
            else -> (3..10).random()
        }
    }

    // -----------------------Deleted lift methods and data-------------------

    val deletedLift = MutableLiveData<Lift>()
    val deletedLiftTags = MutableLiveData<List<String>>()

    fun restoreDeletedLift() {
        deletedLift.value?.let {
            Repository.addLift(it, deletedLiftTags.value ?: emptyList())
        }
    }

    fun clearDeletedLift() {
        deletedLift.value = null
        deletedLiftTags.value = null
    }
}