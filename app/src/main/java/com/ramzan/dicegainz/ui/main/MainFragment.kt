package com.ramzan.dicegainz.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                else -> false
            }
        }

        return binding.root
    }

    private val singleItems = arrayOf("Light", "Dark", "System")

    private fun showThemeDialog(): Boolean {
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
                // Respond to positive button press
                if (newTheme != oldTheme) {
                    with(sharedPrefs.edit()) {
                        putInt(themeString, newTheme)
                        apply()
                    }
                    startActivity(Intent.makeRestartActivityTask(activity?.intent?.component))
                }
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                // Respond to item chose
                newTheme = when (which) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            .show()
        return true
    }
}