package com.nazmar.dicegainz.ui.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.database.*
import com.nazmar.dicegainz.repository.Repository

val tierMap = mapOf(Pair(BOTH, R.string.both), Pair(T1, R.string.t1), Pair(T2, R.string.t2))

class EditorViewModel(val lift: Lift?) : ViewModel() {

    val tags = Repository.allTagsList

    var tagsLoaded = false

    val oldTags = lift?.id?.let { Repository.getTagNamesForLift(it) }

    private var _usedTags = MutableLiveData(mutableSetOf<String>())

    val usedTags: LiveData<MutableSet<String>>
        get() = _usedTags

    // ------------------View setup---------------------------------
    val editorTitleId = if (lift == null) R.string.editorTitleNew else R.string.editorTitleEdit

    val nameInputText = lift?.name ?: ""

    val tier = if (lift == null) tierMap[BOTH] else tierMap[lift.tier]

    val deleteButtonVisible = lift != null

    // -------------------------Methods-----------------------------

    fun addCurrentTag(name: String): Boolean {
        return usedTags.value?.add(name) ?: false
    }

    fun removeCurrentTag(name: String) {
        usedTags.value?.remove(name)
    }

    fun updateLift(lift: Lift) {
        val old = (oldTags?.value ?: emptyList()).toSet()
        val new = usedTags.value!!.filter { !old.contains(it) }.map { Tag(it, lift.id) }
        val deleted = old.filter { !usedTags.value!!.contains(it) }.map { Tag(it, lift.id) }
        Repository.updateLift(lift, new, deleted)
    }

    fun addLift(lift: Lift) {
        Repository.addLift(lift, usedTags.value?.toList() ?: emptyList())
    }

    fun deleteLift(lift: Lift) {
        Repository.deleteLift(lift)
    }
}