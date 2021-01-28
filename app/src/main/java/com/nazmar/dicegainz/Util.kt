package com.nazmar.dicegainz

import android.app.Activity
import android.os.IBinder
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import com.nazmar.dicegainz.ui.roll.Card

const val PREF_KEY_NUM_ROLL_CARDS = "NUM_ROLL_CARDS"

fun Activity.getInputMethodManager(): InputMethodManager {
    return this.getSystemService(InputMethodManager::class.java)
}

fun InputMethodManager.hideKeyboard(windowToken: IBinder) {
    this.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun InputMethodManager.showKeyboard() {
    this.toggleSoftInput(
        InputMethodManager.SHOW_IMPLICIT,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

// For submitting tags when autocomplete item clicked
fun AutoCompleteTextView.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
        }
        true
    }
}

fun MutableLiveData<MutableList<Card.RollCard>>.updateRollResult(
    index: Int,
    updatedResult: String
) {
    val value = this.value?.toMutableList() ?: mutableListOf()
    value[index] = value[index].copy(rollResult = updatedResult)
    this.value = value

}

fun MutableLiveData<MutableList<Card.RollCard>>.updateFilterText(index: Int, updatedText: String) {
    val value = this.value?.toMutableList() ?: mutableListOf()
    value[index] = value[index].copy(filterText = updatedText)
    this.value = value

}