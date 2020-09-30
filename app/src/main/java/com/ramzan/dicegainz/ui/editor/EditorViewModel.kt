package com.ramzan.dicegainz.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.*
import com.ramzan.dicegainz.repository.Repository

val tierMap = mapOf(Pair(BOTH, R.string.both), Pair(T1, R.string.t1), Pair(T2, R.string.t2))

class EditorViewModel(val lift: Lift?, application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

    val tags = repo.allTagsList

    var tagsLoaded = false

    val oldTags = lift?.id?.let { repo.getTagNamesForLift(it) }

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
        repo.updateLift(lift, new, deleted)
    }

    fun addLift(lift: Lift) {
        repo.addLift(lift, usedTags.value?.toList() ?: emptyList())
    }

    fun deleteLift(lift: Lift) {
        repo.deleteLift(lift)
    }
}