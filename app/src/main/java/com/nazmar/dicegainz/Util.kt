package com.nazmar.dicegainz

import android.app.Activity
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections

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

fun NavController.safeNavigate(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let {
        navigate(directions)
    }
}