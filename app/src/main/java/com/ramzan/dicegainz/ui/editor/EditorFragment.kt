package com.ramzan.dicegainz.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ramzan.dicegainz.R
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
            navController.navigate(R.id.action_editorFragment_to_mainFragment)
        }

        return binding.root
    }

    companion object {
        fun newInstance() = EditorFragment()
    }

}