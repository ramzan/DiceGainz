package com.ramzan.dicegainz.ui.roll

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ramzan.dicegainz.R
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

        // Get ViewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = RollViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RollViewModel::class.java)
        binding.rollViewModel = viewModel
        binding.lifecycleOwner = this

        // Set up filter spinners
        viewModel.tagList.observe(viewLifecycleOwner, {
            setUpSpinner(binding.filter1, it)
            setUpSpinner(binding.filter2, it)
            setUpSpinner(binding.filter3, it)
        })

        return binding.root
    }

    private fun setUpSpinner(filter: AutoCompleteTextView, tags: List<String>) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, tags)
        filter.setAdapter(adapter)
        if (filter.text.isNullOrEmpty()) filter.setText(getString(R.string.all), false)
        filter.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            Log.d("butt", "${filter.id} ${filter.text}")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RollFragment()
    }
}