package com.ramzan.dicegainz.ui.roll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.databinding.RollFragmentBinding

/**
 * A placeholder fragment containing a simple view.
 */
class RollFragment : Fragment() {

    private lateinit var binding: RollFragmentBinding

    private lateinit var viewModel: RollViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.roll_fragment, container, false
        )

        // Get ViewModel Factory
        val application = requireNotNull(this.activity).application
        val dataSource = LiftDatabase.getInstance(application).liftDatabaseDao
        val viewModelFactory = RollViewModelFactory(dataSource, application)

        // Get ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(RollViewModel::class.java)
        binding.rollViewModel = viewModel

        binding.lifecycleOwner = this


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RollFragment()
    }
}