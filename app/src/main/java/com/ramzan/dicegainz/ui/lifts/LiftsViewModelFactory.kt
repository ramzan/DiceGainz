package com.ramzan.dicegainz.ui.lifts

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LiftsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiftsViewModel::class.java)) {
            return LiftsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
