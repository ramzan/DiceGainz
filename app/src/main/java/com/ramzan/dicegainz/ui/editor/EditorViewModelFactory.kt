package com.ramzan.dicegainz.ui.editor

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabaseDao

class EditorViewModelFactory(
    private val dataSource: LiftDatabaseDao,
    private val application: Application,
    private val lift: Lift?,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorViewModel::class.java)) {
            return EditorViewModel(dataSource, application, lift) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
