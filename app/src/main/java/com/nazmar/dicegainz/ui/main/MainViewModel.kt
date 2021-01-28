package com.nazmar.dicegainz.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.database.T1
import com.nazmar.dicegainz.database.T2
import com.nazmar.dicegainz.repository.Repository
import com.nazmar.dicegainz.ui.roll.Card
import com.nazmar.dicegainz.updateFilterText
import com.nazmar.dicegainz.updateRollResult
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val lifts = Repository.getAllLifts()

    val tags = Repository.allTagsList

    private var idIndexMap = HashMap<Int, Int>()

    private var _rollCards = MutableLiveData(mutableListOf<Card.RollCard>().apply {
        for (i in 0 until Repository.numCards.value!!) {
            this.add(Card.RollCard(i))
            idIndexMap[i] = i
        }
    })

    val rollCards: LiveData<MutableList<Card.RollCard>>
        get() = _rollCards

    fun addCard() {
        _rollCards.value?.let {
            val id = if (it.isEmpty()) 0 else it.last().id + 1
            it.add(Card.RollCard(id))
            idIndexMap[id] = it.size - 1
            _rollCards.value = it
            Repository.addCard()
        }
    }

    fun deleteCard(id: Int) {
        _rollCards.value?.let {
            idIndexMap[id]?.let { index ->
                it.removeAt(index)
                idIndexMap.remove(id)
                for (i in index until it.size) idIndexMap[it[i].id] = i
                _rollCards.value = it
                Repository.removeCard()
            }
        }
    }

    fun roll(id: Int) {
        rollCards.value?.let { list ->
            idIndexMap[id]?.let { index ->
                viewModelScope.launch {
                    val lift = (tags.value?.let {
                        if (it.contains(list[index].filterText)) {
                            Repository.getLiftsForTagOneShot(list[index].filterText)
                        } else {
                            Repository.getAllLiftsOneShot()
                        }
                    } ?: Repository.getAllLiftsOneShot()).random()
                    _rollCards.updateRollResult(index, "${lift.name} ${getRM(lift.tier)}RM")
                }
            }
        }
    }

    fun updateFilterText(id: Int, text: String) = idIndexMap[id]?.let {
        _rollCards.updateFilterText(it, text)
    }

    fun rollAll() {
        rollCards.value?.let { list ->
            if (!lifts.value.isNullOrEmpty()) list.forEach { roll(it.id) }
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