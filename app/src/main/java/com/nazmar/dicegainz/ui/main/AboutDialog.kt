package com.nazmar.dicegainz.ui.main

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nazmar.dicegainz.R

class AboutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.about))
            .setPositiveButton(getString(R.string.ok), null)
            .setMessage(R.string.about_message)
            .show()
            .also {
                it.findViewById<TextView>(android.R.id.message)?.movementMethod =
                    LinkMovementMethod.getInstance()
            }
    }
}