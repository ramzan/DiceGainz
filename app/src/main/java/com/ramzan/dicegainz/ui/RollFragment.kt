package com.ramzan.dicegainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramzan.dicegainz.R

/**
 * A placeholder fragment containing a simple view.
 */
class RollFragment : Fragment() {

//    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
//            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
//        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_roll, container, false)
        return root
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): RollFragment {
            return RollFragment()
        }
    }
}