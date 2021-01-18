package com.nazmar.dicegainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.MainFragmentBinding
import com.nazmar.dicegainz.ui.SectionsPagerAdapter


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MainFragmentBinding.inflate(inflater)

        binding.viewPager.adapter = SectionsPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.tab_text_1)
            else
                getString(R.string.tab_text_2)
        }.attach()

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
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToThemeDialogFragment())
        return true
    }

    private fun showAboutDialog(): Boolean {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToAboutDialog())
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}