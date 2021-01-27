package com.nazmar.dicegainz.ui.roll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.RollFragmentBinding
import com.nazmar.dicegainz.ui.NoFilterAdapter
import com.nazmar.dicegainz.ui.main.MainFragmentDirections
import com.nazmar.dicegainz.ui.main.MainViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class RollFragment : Fragment() {

    private var _binding: RollFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RollFragmentBinding.inflate(inflater)

        binding.apply {
            welcomeMessage.setOnClickListener {
                showEditDialog()
            }

            // View control
            viewModel.lifts.observe(viewLifecycleOwner) { lifts ->
                loading.visibility = View.GONE
                if (lifts.isNullOrEmpty()) {
                    rollCardList.visibility = View.GONE
                    welcomeMessage.visibility = View.VISIBLE
                } else {
                    rollCardList.visibility = View.VISIBLE
                    welcomeMessage.visibility = View.GONE
                }
            }

            val adapter = RollCardAdapter(object : RollCardAdapter.OnClickListener {
                override fun onRollClick(position: Int) {
                    viewModel.roll(position)
                }

                override fun onFilterClick(position: Int, text: String) {
                    viewModel.updateFilterText(position, text)
                }
            }, resources)

            rollCardList.adapter = adapter

            viewModel.tags.observe(viewLifecycleOwner) { tags ->
                adapter.setTagFilterAdapter(
                    NoFilterAdapter(
                        requireContext(),
                        R.layout.tier_list_item,
                        arrayOf(getString(R.string.all)) + tags.toTypedArray()
                    )
                )
                adapter.notifyDataSetChanged()
            }

            viewModel.rollCards.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }
        return binding.root
    }

    private fun showEditDialog() {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = MainFragmentDirections.actionMainFragmentToEditorFragment()
        navController.navigate(action)
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
