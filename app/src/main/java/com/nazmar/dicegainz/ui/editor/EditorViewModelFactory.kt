package com.nazmar.dicegainz.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditorViewModelFactory(private val liftId: Long) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorViewModel::class.java)) {
            return EditorViewModel(liftId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
