package com.nazmar.dicegainz.ui.roll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.RollFragmentBinding
import com.nazmar.dicegainz.ui.NoFilterAdapter
import com.nazmar.dicegainz.ui.main.MainFragmentDirections
import com.nazmar.dicegainz.ui.main.MainViewModel

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

            RollCardAdapter(object : RollCardAdapter.OnClickListener {
                override fun onRoll(id: Int) = viewModel.roll(id)

                override fun onFilterChange(id: Int, text: String) {
                    viewModel.updateFilterText(id, text)
                }

                override fun onAddCard() = viewModel.addCard()

                override fun onDeleteCard(id: Int) = viewModel.deleteCard(id)
            }, resources).run {

                rollCardList.adapter = this

                viewModel.tags.observe(viewLifecycleOwner) { tags ->
                    this.setTagFilterAdapter(
                        NoFilterAdapter(
                            requireContext(),
                            R.layout.tier_list_item,
                            arrayOf(getString(R.string.all)) + tags.toTypedArray()
                        )
                    )
                    this.notifyDataSetChanged()
                }

                viewModel.rollCards.observe(viewLifecycleOwner, {
                    this.addAddCardAndSubmitList(it as List<Card>)
                })
            }
        }
        return binding.root
    }

    private fun showEditDialog() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditorFragment()
        )
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
