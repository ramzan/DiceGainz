package com.nazmar.dicegainz.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nazmar.dicegainz.R

class ThemeDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val themeStrings = resources.getStringArray(R.array.theme_string_array)
        val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val themeString = getString(R.string.theme)
        val oldTheme = sharedPrefs.getInt(themeString, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val checkedItem = when (oldTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }
        var newTheme = oldTheme

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(themeString)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (newTheme != oldTheme) {
                    sharedPrefs.edit {
                        putInt(themeString, newTheme)
                    }
                    requireActivity().recreate()
                }
            }
            .setSingleChoiceItems(themeStrings, checkedItem) { _, which ->
                newTheme = when (which) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            .show()
    }
}