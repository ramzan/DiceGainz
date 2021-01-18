package com.nazmar.dicegainz.ui.roll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.RollFragmentBinding
import com.nazmar.dicegainz.ui.main.*

/**
 * A placeholder fragment containing a simple view.
 */
class RollFragment : Fragment() {

    private var _binding: RollFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            requireNotNull(
                this.activity
            ).application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RollFragmentBinding.inflate(inflater)

        binding.apply {
            // Set up filter spinners
            viewModel.tagList.observe(viewLifecycleOwner, {
                setUpSpinner(filter1, it, ROLL_FILTER1_ID)
                setUpSpinner(filter2, it, ROLL_FILTER2_ID)
                setUpSpinner(filter3, it, ROLL_FILTER3_ID)
            })
            viewModel.filter1Text.observe(viewLifecycleOwner) {
                binding.filter1.setText(it, false)
            }
            viewModel.filter2Text.observe(viewLifecycleOwner) {
                binding.filter2.setText(it, false)
            }
            viewModel.filter3Text.observe(viewLifecycleOwner) {
                binding.filter3.setText(it, false)
            }
            // Click listeners
            welcomeMessage.setOnClickListener {
                showEditDialog()
            }
            card1.setOnClickListener {
                viewModel.roll(1)
            }
            card2.setOnClickListener {
                viewModel.roll(2)
            }
            card3.setOnClickListener {
                viewModel.roll(3)
            }
            rollAllButton.setOnClickListener {
                viewModel.rollAll()
            }
            // View control
            viewModel.liftsLoaded.observe(viewLifecycleOwner) { liftsLoaded ->
                if (liftsLoaded) {
                    rollAllButton.visibility = View.VISIBLE
                    rollView.visibility = View.VISIBLE
                    welcomeMessage.visibility = View.GONE
                } else {
                    rollAllButton.visibility = View.GONE
                    rollView.visibility = View.GONE
                    welcomeMessage.visibility = View.VISIBLE
                }
            }
            // Card roll text
            viewModel.lift1Text.observe(viewLifecycleOwner) {
                lift1Text.text = if (it.isNullOrBlank()) {
                    getString(R.string.tap_to_roll)
                } else it
            }
            viewModel.lift2Text.observe(viewLifecycleOwner) {
                lift2Text.text = if (it.isNullOrBlank()) {
                    getString(R.string.tap_to_roll)
                } else it
            }
            viewModel.lift3Text.observe(viewLifecycleOwner) {
                lift3Text.text = if (it.isNullOrBlank()) {
                    getString(R.string.tap_to_roll)
                } else it
            }
        }
        return binding.root
    }

    private fun showEditDialog() {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = MainFragmentDirections.actionMainFragmentToEditorFragment(null)
        navController.navigate(action)
    }

    private fun setUpSpinner(filter: AutoCompleteTextView, tags: List<String>, id: Int) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, tags)
        filter.setAdapter(adapter)
        if (!tags.contains(filter.text.toString())) {
            viewModel.updateFilterText(id, getString(R.string.all))
        }
        filter.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            viewModel.updateFilterText(id, filter.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = RollFragment()
    }
}
