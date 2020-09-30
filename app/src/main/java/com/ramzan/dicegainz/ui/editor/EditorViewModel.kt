package com.ramzan.dicegainz.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.*
import com.ramzan.dicegainz.repository.Repository

val tierMap = mapOf(Pair(BOTH, R.string.both), Pair(T1, R.string.t1), Pair(T2, R.string.t2))

class EditorViewModel(val lift: Lift?, application: Application) : AndroidViewModel(application) {

    private val repo = Repository(LiftDatabase.getInstance(application))

    val tags = repo.allTagsList

    val oldTags = lift?.id?.let { repo.getTagNamesForLift(it) }

    var usedTags = MutableLiveData(mutableListOf<String>())

    // ------------------View setup---------------------------------
    val editorTitleId = if (lift == null) R.string.editorTitleNew else R.string.editorTitleEdit

    val nameInputText = lift?.name ?: ""

    val tier = if (lift == null) tierMap[BOTH] else tierMap[lift.tier]

    val deleteButtonVisible = lift != null

    // -------------------------Methods-----------------------------

    fun addCurrentTag(name: String): Boolean {
        usedTags.value?.apply {
            if (!contains(name)) {
                add(name)
                return true
            }
        }
        return false
    }

    fun updateLift(lift: Lift) {
        repo.updateLift(lift)
    }

    fun addLift(lift: Lift) {
        repo.addLift(lift)
    }


    fun deleteLift(lift: Lift) {
        repo.deleteLift(lift)
    }
}