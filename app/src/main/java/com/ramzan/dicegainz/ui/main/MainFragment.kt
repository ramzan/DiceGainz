package com.ramzan.dicegainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        return binding.root
    }
}