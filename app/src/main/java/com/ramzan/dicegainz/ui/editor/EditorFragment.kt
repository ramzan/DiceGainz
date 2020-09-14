package com.ramzan.dicegainz.ui.editor

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.databinding.EditorFragmentBinding


class EditorFragment : Fragment() {

    private val tierStrings = listOf("T1 and T2", "T1", "T2")

    private lateinit var binding: EditorFragmentBinding

    private lateinit var editorViewModel: EditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.editor_fragment, container, false
        )

        // Get lift from args
        val args = EditorFragmentArgs.fromBundle(requireArguments())

        // Get ViewModel Factory
        val application = requireNotNull(this.activity).application
        val dataSource = LiftDatabase.getInstance(application).liftDatabaseDao
        val viewModelFactory = EditorViewModelFactory(dataSource, application)

        // Get ViewModel
        editorViewModel = ViewModelProvider(this, viewModelFactory).get(EditorViewModel::class.java)

        binding.apply {
            viewModel = editorViewModel

            // Tier selector
            val adapter = ArrayAdapter(requireContext(), R.layout.tier_list_item, tierStrings)
            tierSelector.setAdapter(adapter)
            tierSelector.setText("T1 and T2", false)

            // Edit lift mode
            if (args.selectedLift !== null) {
                val lift: Lift = args.selectedLift!!

                nameInput.setText(lift.name)

                editorToolbar.title = getString(R.string.editorTitleEdit)

                tierSelector.setText(tierStrings[lift.tier], false)

                val deleteButton = editorToolbar.menu.getItem(0)

                deleteButton.setOnMenuItemClickListener {
                    deleteLift(lift)
                    goBack()
                    true
                }

                deleteButton.isVisible = true

                // New lift mode
            } else {
                editorToolbar.title = getString(R.string.editorTitleNew)
            }

            val saveButton = editorToolbar.menu.getItem(1)

            saveButton.setOnMenuItemClickListener {
                saveLift(args.selectedLift)
                goBack()
                true
            }


            editorToolbar.setNavigationOnClickListener {
                goBack()
            }


            // Tag chips
            val genres = mutableListOf("Full body", "Upper", "Lower", "Push", "Pull")
            genres.forEach { chipGroup.addView(getChip(it)) }

        }

        return binding.root
    }


    private fun getChip(text: String): Chip {
        val chip = Chip(requireContext())
        chip.setChipDrawable(ChipDrawable.createFromResource(requireContext(), R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f,
            resources.displayMetrics
        ).toInt()
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCloseIconClickListener { binding.chipGroup.removeView(chip) }
        return chip
    }

    private fun saveLift(lift: Lift?) {
        val name = binding.nameInput.text.toString()
        val tier = tierStrings.indexOf(binding.tierSelector.text.toString())
        if (lift !== null) {
            lift.name = name
            lift.tier = tier
            editorViewModel.updateLift(lift)
        } else {
            editorViewModel.addLift(Lift(name, tier))
        }
    }

    private fun deleteLift(lift: Lift) {
        editorViewModel.deleteLift(lift)

    }

    private fun goBack() {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        navController.navigate(EditorFragmentDirections.actionEditorFragmentToMainFragment())
    }
}