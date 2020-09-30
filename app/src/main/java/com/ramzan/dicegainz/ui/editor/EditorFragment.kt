package com.ramzan.dicegainz.ui.editor

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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

    private lateinit var tierStrings: Array<String>
    private lateinit var binding: EditorFragmentBinding
    private lateinit var editorViewModel: EditorViewModel
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.editor_fragment, container, false
        )

        val args = EditorFragmentArgs.fromBundle(requireArguments())

        // Get ViewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = EditorViewModelFactory(args.selectedLift, application)
        editorViewModel = ViewModelProvider(this, viewModelFactory).get(EditorViewModel::class.java)

        binding.apply {
            viewModel = editorViewModel

            // Toolbar settings
            editorToolbar.apply {
                title = getText(editorViewModel.editorTitleId)
                setNavigationOnClickListener {
                    goBack()
                }

                val deleteButton = menu.getItem(0)
                val saveButton = menu.getItem(1)

                deleteButton.isVisible = editorViewModel.deleteButtonVisible
                deleteButton.setOnMenuItemClickListener {
                    deleteLift(args.selectedLift!!)
                    goBack(args.selectedLift!!)
                    true
                }

                saveButton.setOnMenuItemClickListener {
                    if (TextUtils.isEmpty(binding.nameInput.text?.trim())) {
                        binding.nameInput.setText("")
                        nameInputLayout.isErrorEnabled = true
                        nameInputLayout.error = getString(R.string.empty_name_error_msg)
                    } else {
                        saveLift(args.selectedLift)
                        goBack()
                    }
                    true
                }
            }

            // Show the keyboard.
            imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
            nameInput.apply {
                setText(editorViewModel.nameInputText)
                nameInput.requestFocus()
            }

            // Tier selector
            tierStrings = resources.getStringArray(R.array.tier_string_array)
            val adapter = ArrayAdapter(requireContext(), R.layout.tier_list_item, tierStrings)
            tierSelector.apply {
                setAdapter(adapter)
                setText(getText(editorViewModel.tier!!), false)
            }

            // Tag editor
            chipCreator.apply {
                onSubmit {
                    addNewTag()
                }
                onItemClickListener = OnItemClickListener { _, _, _, _ ->
                    addNewTag()
                }
            }

            // Tag editor data
            editorViewModel.apply {
                // Populate tag autocomplete with all preexisting tags
                tags.observe(viewLifecycleOwner, {
                    chipCreator.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            R.layout.tier_list_item,
                            it
                        )
                    )
                })
                // Load existing tags for lift into chips
                oldTags?.observe(viewLifecycleOwner, { tags ->
                    if (!editorViewModel.tagsLoaded) {
                        tags.forEach {
                            addChip(getChip(it))
                            editorViewModel.addCurrentTag((it))
                        }
                        editorViewModel.tagsLoaded = true
                    }
                })
                // Refresh when new tag added
                usedTags.observe(viewLifecycleOwner, { tags ->
                    tags.forEach { addChip(getChip(it)) }
                })
            }

        }
        return binding.root
    }

    private fun addChip(chip: Chip) {
        binding.chipGroup.addView(chip)
    }

    private fun getChip(chipText: String): Chip {
        val chip = Chip(requireContext())
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f,
            resources.displayMetrics
        ).toInt()

        return chip.apply {
            setChipDrawable(ChipDrawable.createFromResource(requireContext(), R.xml.my_chip))
            setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            text = chipText
            setOnCloseIconClickListener {
                editorViewModel.removeCurrentTag(chipText)
                binding.chipGroup.removeView(it)
            }
        }
    }

    private fun addNewTag() {
        val tagName = binding.chipCreator.text.toString().trim()
        if (tagName.isNotEmpty() and editorViewModel.addCurrentTag((tagName))) {
            addChip(getChip(tagName))
        }
        binding.chipCreator.setText("")
    }

    private fun saveLift(lift: Lift?) {
        val name = binding.nameInput.text.toString().trim()
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

    // For submitting tags when autocomplete item clicked
    private fun AutoCompleteTextView.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                func()
            }
            true
        }
    }
}