package com.ramzan.dicegainz.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.BOTH
import com.ramzan.dicegainz.database.T1
import com.ramzan.dicegainz.database.T2
import com.ramzan.dicegainz.databinding.EditorFragmentBinding

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

        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)

        binding.button.setOnClickListener {
            navController.navigate(EditorFragmentDirections.actionEditorFragmentToMainFragment())
        }

        val args = EditorFragmentArgs.fromBundle(requireArguments())

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

        return binding.root
    }

    companion object {
        fun newInstance() = EditorFragment()
    }

}