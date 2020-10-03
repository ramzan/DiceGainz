package com.ramzan.dicegainz.ui.roll

import android.os.Bundle
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
import com.ramzan.dicegainz.ui.main.MainViewModel
import com.ramzan.dicegainz.ui.main.MainViewModelFactory

/**
 * A placeholder fragment containing a simple view.
 */
class RollFragment : Fragment() {

    private lateinit var binding: RollFragmentBinding

    private lateinit var viewModel: MainViewModel

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
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        ).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set up filter spinners
        viewModel.tagList.observe(viewLifecycleOwner, {
            setUpSpinner(binding.filter1, it, 1)
            setUpSpinner(binding.filter2, it, 2)
            setUpSpinner(binding.filter3, it, 3)
        })

        return binding.root
    }

    private fun setUpSpinner(filter: AutoCompleteTextView, tags: List<String>, id: Int) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, tags)
        filter.setAdapter(adapter)
        if (filter.text.isNullOrEmpty()) filter.setText(getString(R.string.all), false)
        filter.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            viewModel.updateFilterText(id, filter.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RollFragment()
    }
}
