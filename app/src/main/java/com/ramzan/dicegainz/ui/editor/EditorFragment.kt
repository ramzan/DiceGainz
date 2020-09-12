package com.ramzan.dicegainz.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.BOTH
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.T1
import com.ramzan.dicegainz.database.T2
import com.ramzan.dicegainz.databinding.EditorFragmentBinding
import com.ramzan.dicegainz.ui.lifts.LiftsViewModel
import com.ramzan.dicegainz.ui.lifts.LiftsViewModelFactory

class EditorFragment : Fragment() {

    private lateinit var binding: EditorFragmentBinding

    private lateinit var viewModel: EditorViewModel

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
        val viewModelFactory = EditorViewModelFactory(dataSource, application, args.selectedLift)

        // Get ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditorViewModel::class.java)
        binding.viewModel = viewModel


        binding.liftNameInput.setText(args.selectedLift?.name ?: "")

        binding.editorTitle.text = if (args.selectedLift == null) "New lift" else "Edit lift"

        when (args.selectedLift?.tier) {
            BOTH -> {
                binding.t1checkBox.isChecked = true
                binding.t2checkBox.isChecked = true
            }
            T1 -> binding.t1checkBox.isChecked = true
            T2 -> binding.t2checkBox.isChecked = true
        }

        binding.button.setOnClickListener {
            goBack()
        }

        args.selectedLift?.let { binding.deleteButton.visibility = VISIBLE }

        binding.deleteButton.setOnClickListener {
            args.selectedLift?.let { it1 -> viewModel.deleteLift(it1) }
            goBack()
        }

        return binding.root
    }

    private fun goBack() {
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        navController.navigate(EditorFragmentDirections.actionEditorFragmentToMainFragment())
    }

    companion object {
        fun newInstance() = EditorFragment()
    }

}