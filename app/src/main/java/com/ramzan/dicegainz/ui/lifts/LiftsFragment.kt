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
import com.ramzan.dicegainz.MainFragmentDirections
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.databinding.LiftsFragmentBinding

class LiftsFragment : Fragment() {

    private lateinit var binding: LiftsFragmentBinding

    private lateinit var viewModel: LiftsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.lifts_fragment, container, false
        )

        // Get ViewModel Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LiftsViewModelFactory(application)

        // Get ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(LiftsViewModel::class.java)
        binding.liftsViewModel = viewModel

        // Get navController
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)

        // Set up filter spinner
        viewModel.tagList.observe(viewLifecycleOwner, {
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, it)
            binding.filterBar.setAdapter(adapter)
        })
        binding.filterBar.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            viewModel.filterLifts(binding.filterBar.text.toString())
        }

        // Set the recyclerview adapter
        val adapter = LiftAdapter(LiftAdapter.OnClickListener {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToEditorFragment(
                    it
                )
            )
        })

        binding.liftList.adapter = adapter

        viewModel.lifts.observe(viewLifecycleOwner, { it2 ->
            it2?.let { it1 ->
                it1.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        })

        binding.lifecycleOwner = this

        // Show undo snackbar for deleted lift
        val deletedLift = arguments?.get("deletedLift")
        deletedLift?.let {
            Snackbar.make(binding.root, getString(R.string.lift_deleted), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.undo)) {
                    @Suppress("UNCHECKED_CAST")
                    val deletedTags = arguments?.get("deletedTags") as Array<String>
                    viewModel.addLift(deletedLift as Lift, deletedTags.toList())
                }
                .setAnchorView(binding.fab)
                .show()
            arguments?.putParcelable("deletedLift", null)
        }

        binding.fab.setOnClickListener {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToEditorFragment(null)
            )
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(deletedLift: Lift?, deletedTags: Array<String>?): LiftsFragment {
            val args = Bundle()
            args.putParcelable("deletedLift", deletedLift)
            args.putStringArray("deletedTags", deletedTags)
            val liftsFragment = LiftsFragment()
            liftsFragment.arguments = args
            return liftsFragment
        }
    }
}