package com.ramzan.dicegainz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ramzan.dicegainz.databinding.MainFragmentBinding
import com.ramzan.dicegainz.ui.SectionsPagerAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment, container, false
        )

        val fm: FragmentManager = childFragmentManager
        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), fm)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment()
    }
}