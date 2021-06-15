package com.nazmar.dicegainz.ui.lifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.databinding.LiftsFragmentBinding
import com.nazmar.dicegainz.safeNavigate
import com.nazmar.dicegainz.ui.BaseFragment
import com.nazmar.dicegainz.ui.main.MainFragmentDirections

class LiftsFragment : BaseFragment<LiftsFragmentBinding>() {

    private val viewModel: LiftsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpBinding(LiftsFragmentBinding.inflate(inflater))

        // Set up filter spinner
        viewModel.tagList.observe(viewLifecycleOwner) {
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.tier_list_item,
                    listOf(getString(R.string.all)) + it
                )
            binding.filterBar.setAdapter(adapter)
        }
        viewModel.liftsFilterText.observe(viewLifecycleOwner) {
            binding.filterBar.setText(it, false)
        }
        binding.filterBar.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            viewModel.updateFilterText(binding.filterBar.text.toString())
        }

        LiftAdapter(LiftAdapter.OnClickListener {
            showEditDialog(it)
        }).run {
            binding.liftList.adapter = this

            viewModel.lifts.observe(viewLifecycleOwner, {
                this.submitList(it)
            })
        }

        return binding.root
    }

    private fun showEditDialog(lift: Lift) {
        findNavController().safeNavigate(
            MainFragmentDirections.actionMainFragmentToEditorFragment().apply {
                liftId = lift.id
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiftsFragment()
    }
}
