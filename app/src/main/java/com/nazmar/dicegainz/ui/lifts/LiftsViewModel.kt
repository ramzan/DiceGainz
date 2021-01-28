package com.nazmar.dicegainz.ui.lifts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.repository.Repository

class LiftsViewModel : ViewModel() {

    val tagList = Repository.allTagsList

    private var _liftsFilterText = MutableLiveData("")

    val liftsFilterText: LiveData<String>
        get() = _liftsFilterText

    fun updateFilterText(tag: String) {
        _liftsFilterText.value = tag
    }

    private val _lifts: LiveData<List<Lift>> = Transformations.switchMap(_liftsFilterText) { tag ->
        tagList.value?.let {
            if (it.contains(tag)) Repository.getLiftsForTag(tag) else Repository.getAllLifts()
        } ?: Repository.getAllLifts()

    }

    val lifts: LiveData<List<Lift>>
        get() = _lifts
}