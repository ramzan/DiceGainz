package com.nazmar.dicegainz.ui.lifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.databinding.LiftsFragmentBinding
import com.nazmar.dicegainz.ui.main.LIFTS_FILTER_ID
import com.nazmar.dicegainz.ui.main.MainFragmentDirections
import com.nazmar.dicegainz.ui.main.MainViewModel
import com.nazmar.dicegainz.ui.main.MainViewModelFactory

class LiftsFragment : Fragment() {

    private var _binding: LiftsFragmentBinding? = null
    private val binding get() = _binding!!
    val viewModel: MainViewModel by activityViewModels { MainViewModelFactory(requireNotNull(this.activity).application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LiftsFragmentBinding.inflate(inflater)

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
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) } //400 ms
            showEditDialog(null)
        }

        return binding.root
    }

    private fun showEditDialog(lift: Lift?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = MainFragmentDirections.actionMainFragmentToEditorFragment(lift)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiftsFragment()
    }
}
