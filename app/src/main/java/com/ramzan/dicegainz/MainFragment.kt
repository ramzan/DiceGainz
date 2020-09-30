package com.ramzan.dicegainz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        val args = MainFragmentArgs.fromBundle(requireArguments())
        val fm = childFragmentManager

        binding.viewPager.apply {
            adapter = SectionsPagerAdapter(requireContext(), fm, args.deletedLift, args.deletedTags)
            currentItem = args.tabIndex
            binding.tabs.setupWithViewPager(this)
        }

        return binding.root
    }
}