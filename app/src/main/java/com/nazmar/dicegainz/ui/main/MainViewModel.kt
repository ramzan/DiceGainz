package com.nazmar.dicegainz.ui.main

import androidx.lifecycle.*
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.database.T1
import com.nazmar.dicegainz.database.T2
import com.nazmar.dicegainz.repository.Repository
import com.nazmar.dicegainz.ui.roll.Card
import kotlinx.coroutines.launch

private const val STATE_FILTER_TEXTS = "filterTexts"
private const val STATE_ROLL_RESULTS = "rollResults"

class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

    val lifts = Repository.getAllLifts()

    val tags = Repository.allTagsList

    private var idIndexMap = HashMap<Int, Int>()

    private var _rollCards = MutableLiveData(mutableListOf<Card.RollCard>().apply {
        val filterTexts: List<String>? = state.get(STATE_FILTER_TEXTS)
        val rollResults: List<String>? = state.get(STATE_ROLL_RESULTS)
        for (i in 0 until Repository.numCards.value!!) {
            this.add(
                Card.RollCard(
                    i,
                    filterTexts?.get(i) ?: "",
                    rollResults?.get(i) ?: ""
                )
            )
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
            saveRollResultsToBundle()
            saveFilterTextsToBundle()
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
                saveRollResultsToBundle()
                saveFilterTextsToBundle()
            }
        }
    }

    fun roll(id: Int) {
        rollCards.value?.let { list ->
            idIndexMap[id]?.let { index ->
                viewModelScope.launch {
                    val lift = Repository.getRandomLiftForTag(list[index].filterText)
                    _rollCards.updateRollResult(index, "${lift.name} ${getRM(lift.tier)}RM")
                    saveRollResultsToBundle()
                }
            }
        }
    }

    fun rollAll() {
        rollCards.value?.let { list ->
            if (!lifts.value.isNullOrEmpty()) list.forEach { roll(it.id) }
        }
    }

    fun updateFilterText(id: Int, text: String) = idIndexMap[id]?.let { index ->
        _rollCards.updateFilterText(index, text)
        saveFilterTextsToBundle()
    }

    private fun getRM(tier: Int): Int {
        return when (tier) {
            T1 -> (3..6).random()
            T2 -> (6..10).random()
            else -> (3..10).random()
        }
    }

    private fun MutableLiveData<MutableList<Card.RollCard>>.updateRollResult(
        index: Int,
        updatedResult: String
    ) {
        val value = this.value?.toMutableList() ?: mutableListOf()
        value[index] = value[index].copy(rollResult = updatedResult)
        this.value = value

    }

    private fun MutableLiveData<MutableList<Card.RollCard>>.updateFilterText(
        index: Int,
        updatedText: String
    ) {
        val value = this.value?.toMutableList() ?: mutableListOf()
        value[index] = value[index].copy(filterText = updatedText)
        this.value = value

    }

    private fun saveRollResultsToBundle() =
        state.set(STATE_ROLL_RESULTS, rollCards.value!!.map { it.rollResult })

    private fun saveFilterTextsToBundle() =
        state.set(STATE_FILTER_TEXTS, rollCards.value!!.map { it.filterText })


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