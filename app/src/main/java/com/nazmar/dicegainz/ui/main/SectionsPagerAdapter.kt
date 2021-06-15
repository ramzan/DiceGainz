package com.nazmar.dicegainz.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nazmar.dicegainz.ui.lifts.LiftsFragment
import com.nazmar.dicegainz.ui.roll.RollFragment

class SectionsPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RollFragment.newInstance()
            else -> LiftsFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}