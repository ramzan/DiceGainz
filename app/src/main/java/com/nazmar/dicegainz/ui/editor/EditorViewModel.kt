package com.nazmar.dicegainz.ui.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.database.BOTH
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.database.Tag
import com.nazmar.dicegainz.repository.Repository
import kotlinx.coroutines.launch

class EditorViewModel(val liftId: Long) : ViewModel() {

    val tags = Repository.allTagsList

    private var _state = MutableLiveData<EditorViewState>(EditorViewState.Loading)

    val state: LiveData<EditorViewState>
        get() = _state

    init {
        if (liftId == 0L) {
            _state.value = EditorViewState.New()
        } else {
            viewModelScope.launch {
                Repository.getLift(liftId)?.let {
                    _state.value = EditorViewState.Editing(
                        lift = it,
                        name = it.name,
                        tier = it.tier,
                        oldTags = Repository.getTagNamesForLift(liftId),
                    ).apply {
                        currentTags.addAll(oldTags)
                    }
                }
            }
        }
    }
}


sealed class EditorViewState {

    abstract var name: String
    abstract var tier: Int
    abstract var currentTags: MutableList<String>

    abstract fun saveLift()

    fun addCurrentTag(name: String): Boolean {
        return currentTags.run {
            if (!this.contains(name)) {
                add(name)
                true
            } else false
        }
    }

    fun removeCurrentTag(name: String) = currentTags.remove(name)

    object Loading : EditorViewState() {
        override var name = ""
        override var tier = 0
        override var currentTags = mutableListOf<String>()
        override fun saveLift() = Unit
    }

    data class Editing(
        val lift: Lift,
        override var name: String,
        override var tier: Int,
        val oldTags: List<String>,
        override var currentTags: MutableList<String> = mutableListOf(),
        val editorTitleId: Int = R.string.editorTitleEdit
    ) : EditorViewState() {

        override fun saveLift() {
            val old = oldTags.toSet()
            Repository.updateLift(
                lift.copy(name = name, tier = tier),
                currentTags.filter { !old.contains(it) }.map { Tag(it, lift.id) },
                old.filter { !currentTags.contains(it) }.map { Tag(it, lift.id) }
            )
        }

        fun deleteLift() = Repository.deleteLift(lift)
    }

    data class New(
        override var name: String = "",
        override var tier: Int = BOTH,
        override var currentTags: MutableList<String> = mutableListOf(),
        val editorTitleId: Int = R.string.editorTitleNew
    ) : EditorViewState() {

        override fun saveLift() = Repository.addLift(Lift(name, tier), currentTags.toList())
    }
}
