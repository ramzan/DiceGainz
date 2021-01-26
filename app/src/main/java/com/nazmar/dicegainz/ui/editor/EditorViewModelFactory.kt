package com.nazmar.dicegainz.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nazmar.dicegainz.database.Lift

class EditorViewModelFactory(private val lift: Lift?) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorViewModel::class.java)) {
            return EditorViewModel(lift) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
