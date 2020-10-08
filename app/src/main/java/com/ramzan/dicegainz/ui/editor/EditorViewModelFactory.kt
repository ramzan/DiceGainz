package com.ramzan.dicegainz.ui.editor

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramzan.dicegainz.database.Lift

class EditorViewModelFactory(
    private val lift: Lift?,
    private val application: Application,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorViewModel::class.java)) {
            return EditorViewModel(lift, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
