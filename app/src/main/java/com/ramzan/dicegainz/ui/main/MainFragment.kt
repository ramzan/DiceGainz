package com.ramzan.dicegainz.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.databinding.MainFragmentBinding
import com.ramzan.dicegainz.ui.SectionsPagerAdapter


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment, container, false
        )

        binding.viewPager.apply {
            adapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
            binding.tabs.setupWithViewPager(this)
        }

        binding.mainToolbar.setOnMenuItemClickListener {
            when (it) {
                binding.mainToolbar.menu[0] -> showThemeDialog()
                binding.mainToolbar.menu[1] -> showAboutDialog()
                else -> false
            }
        }

        return binding.root
    }

    private fun showThemeDialog(): Boolean {
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

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(themeString)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (newTheme != oldTheme) {
                    with(sharedPrefs.edit()) {
                        putInt(themeString, newTheme)
                        apply()
                    }
                    startActivity(Intent.makeRestartActivityTask(activity?.intent?.component))
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
        return true
    }

    private fun showAboutDialog(): Boolean {
        val alert = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.about))
            .setPositiveButton(getString(R.string.ok), null)
            .setMessage(R.string.about_message)
            .create()

        alert.show()
        alert.findViewById<TextView>(android.R.id.message)?.movementMethod =
            LinkMovementMethod.getInstance()
        return true
    }

}