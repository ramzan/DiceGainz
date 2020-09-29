package com.ramzan.dicegainz.ui.editor

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.databinding.EditorFragmentBinding


class EditorFragment : Fragment() {


//    private var categories = mutableListOf("Full body", "Upper", "Lower", "Push", "Pull")

    private lateinit var tierStrings: Array<String>

    private lateinit var binding: EditorFragmentBinding

    private lateinit var editorViewModel: EditorViewModel

    private lateinit var imm: InputMethodManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.editor_fragment, container, false
        )

        // Get tier string array
        tierStrings = resources.getStringArray(R.array.tier_string_array)

        // Get lift from args
        val args = EditorFragmentArgs.fromBundle(requireArguments())

        // Get ViewModel Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = EditorViewModelFactory(args.selectedLift, application)

        // Get ViewModel
        editorViewModel = ViewModelProvider(this, viewModelFactory).get(EditorViewModel::class.java)

        binding.apply {
            viewModel = editorViewModel

            // Tier selector
            val adapter = ArrayAdapter(requireContext(), R.layout.tier_list_item, tierStrings)
            tierSelector.setAdapter(adapter)
            tierSelector.setText(getText(R.string.both), false)

            // Tag selector
            editorViewModel.tags.observe(viewLifecycleOwner, {
                chipCreator.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.tier_list_item,
                        it.map { t -> t.name })
                )
            })

            // Name input text
            nameInput.setText(editorViewModel.nameInputText)

            // Tier selection
            tierSelector.setText(getText(editorViewModel.tier!!), false)

            val deleteButton = editorToolbar.menu.getItem(0)

            deleteButton.isVisible = editorViewModel.deleteButtonVisible

            deleteButton.setOnMenuItemClickListener {
                deleteLift(args.selectedLift!!)
                goBack(args.selectedLift!!)
                true
            }

            editorToolbar.title = getText(editorViewModel.editorTitleId)

            val saveButton = editorToolbar.menu.getItem(1)

            saveButton.setOnMenuItemClickListener {
                if (TextUtils.isEmpty(binding.nameInput.text)) {
                    nameInputLayout.isErrorEnabled = true
                    nameInputLayout.error = getString(R.string.empty_name_error_msg)
                } else {
                    saveLift(args.selectedLift)
                    goBack()
                }
                true
            }


            editorToolbar.setNavigationOnClickListener {
                goBack()
            }

            chipCreator.onSubmit {
                if (binding.chipCreator.text.isNotEmpty()) {
                    addTag()
                }
            }

            chipCreator.onItemClickListener = OnItemClickListener { _, _, _, _ ->
                addTag()
            }

            // Tag chips
            editorViewModel.usedTags.observe(viewLifecycleOwner, { tags ->
                tags.forEach { addChip(getChip(it)) }
            })

            // Show the keyboard.
            imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
            // Set the focus to the edit text.
            nameInput.requestFocus()

        }

        return binding.root
    }

    private fun addTag() {
        addChip(getChip(binding.chipCreator.text.toString()))
        editorViewModel.addTag((binding.chipCreator.text.toString()))
        binding.chipCreator.setText("")
        Log.d("tags", editorViewModel.usedTags.value.toString())
    }

    private fun AutoCompleteTextView.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                func()
            }

            true

        }
    }

    private fun addChip(chip: Chip) {
        binding.chipGroup.addView(chip)
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
        goBack(null)
    }

    private fun goBack(deletedLift: Lift?) {
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = EditorFragmentDirections.actionEditorFragmentToMainFragment()
        action.deletedLift = deletedLift
        navController.navigate(action)
    }
}