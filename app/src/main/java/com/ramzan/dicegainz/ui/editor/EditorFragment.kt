package com.ramzan.dicegainz.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.*
import com.ramzan.dicegainz.databinding.EditorFragmentBinding

class EditorFragment : Fragment() {

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

            // Edit lift mode
            if (args.selectedLift !== null) {
                val lift: Lift = args.selectedLift!!

                nameInput.setText(lift.name)

                editorTitle.text = getString(R.string.editorTitleEdit)

                when (lift.tier) {
                    T1 -> radioButtonT1.isChecked = true
                    T2 -> radioButtonT2.isChecked = true
                }

                deleteButton.setOnClickListener {
                    deleteLift(lift)
                    goBack()
                }

                deleteButton.visibility = VISIBLE

                // New lift mode
            } else {
                editorTitle.text = getString(R.string.editorTitleNew)
            }

            cancelButton.setOnClickListener {
                goBack()
            }

            saveButton.setOnClickListener {
                saveLift(args.selectedLift)
                goBack()
            }

        }

        return binding.root
    }

    private fun saveLift(lift: Lift?) {
        val name = binding.nameInput.text.toString()
        val tier =
            when (binding.tierSelector.checkedRadioButtonId) {
                R.id.radio_button_t1 -> T1
                R.id.radio_button_t2 -> T2
                else -> BOTH
            }
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

    companion object {
        fun newInstance() = EditorFragment()
    }

}