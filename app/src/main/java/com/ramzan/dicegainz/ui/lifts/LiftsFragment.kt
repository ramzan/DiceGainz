package com.ramzan.dicegainz.ui.lifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.databinding.LiftsFragmentBinding
import com.ramzan.dicegainz.ui.main.LIFTS_FILTER_ID
import com.ramzan.dicegainz.ui.main.MainFragmentDirections
import com.ramzan.dicegainz.ui.main.MainViewModel
import com.ramzan.dicegainz.ui.main.MainViewModelFactory

class LiftsFragment : Fragment() {

    private lateinit var binding: LiftsFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.lifts_fragment, container, false
        )

        // Get ViewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        // Show undo snackbar for deleted lift
        viewModel.deletedLift.observe(viewLifecycleOwner) { deletedLift ->
            deletedLift?.let {
                Snackbar.make(binding.root, getString(R.string.lift_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) { viewModel.restoreDeletedLift() }
                    .setAnchorView(binding.fab)
                    .show()
            }
        }


        // Set up filter spinner
        viewModel.tagList.observe(viewLifecycleOwner) {
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, it)
            binding.filterBar.setAdapter(adapter)
        }
        viewModel.liftsFilterText.observe(viewLifecycleOwner) {
            binding.filterBar.setText(it, false)
        }
        binding.filterBar.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            viewModel.updateFilterText(LIFTS_FILTER_ID, binding.filterBar.text.toString())
        }

        // Set the recyclerview adapter
        val adapter = LiftAdapter(LiftAdapter.OnClickListener {
            showEditDialog(it)
        })

        binding.liftList.adapter = adapter

        viewModel.lifts.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        binding.fab.setOnClickListener {
            showEditDialog(null)
        }

        return binding.root
    }

    private fun showEditDialog(lift: Lift?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = MainFragmentDirections.actionMainFragmentToEditorFragment(lift)
        navController.navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiftsFragment()
    }
}
