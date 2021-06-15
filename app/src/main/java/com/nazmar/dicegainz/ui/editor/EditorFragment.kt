package com.nazmar.dicegainz.ui.editor

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.database.BOTH
import com.nazmar.dicegainz.database.T1
import com.nazmar.dicegainz.database.T2
import com.nazmar.dicegainz.databinding.EditorFragmentBinding
import com.nazmar.dicegainz.getInputMethodManager
import com.nazmar.dicegainz.hideKeyboard
import com.nazmar.dicegainz.showKeyboard
import com.nazmar.dicegainz.ui.BaseFragment
import com.nazmar.dicegainz.ui.main.MainViewModel


class EditorFragment : BaseFragment<EditorFragmentBinding>() {

    private lateinit var tierStrings: Array<String>

    private val editorViewModel: EditorViewModel by viewModels {
        EditorViewModelFactory(requireArguments().getLong("liftId"))
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var imm: InputMethodManager

    private val tierMap =
        mapOf(Pair(BOTH, R.string.both), Pair(T1, R.string.t1), Pair(T2, R.string.t2))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpBinding(EditorFragmentBinding.inflate(inflater))

        imm = requireActivity().getInputMethodManager()

        binding.apply {
            editorToolbar.apply {
                setNavigationOnClickListener {
                    goBack()
                }
                menu.findItem(R.id.editor_save_btn).setOnMenuItemClickListener {
                    saveLift()
                    true
                }
            }

            // Tier selector
            tierStrings = resources.getStringArray(R.array.tier_string_array)
            tierSelector.setAdapter(
                NoFilterAdapter(requireContext(), R.layout.tier_list_item, tierStrings)
            )

            // Tag editor
            chipCreator.apply {
                onSubmit {
                    addNewTag()
                }
                onItemClickListener = OnItemClickListener { _, _, _, _ ->
                    addNewTag()
                }

                // Populate autocomplete with all preexisting tags
                editorViewModel.tags.observe(viewLifecycleOwner) {
                    setAdapter(ArrayAdapter(requireContext(), R.layout.tier_list_item, it))
                }
            }

            chipCreatorContainer.setEndIconOnClickListener {
                addNewTag()
            }
        }

        editorViewModel.state.observe(viewLifecycleOwner) { state ->

            binding.apply {
                nameInput.setText(state.name)

                tierSelector.apply {
                    setText(tierMap[state.tier]?.let { getString(it) }, false)
                }
            }

            when (state) {
                is EditorViewState.New -> {
                    binding.editorToolbar.apply {
                        title = getText(state.editorTitleId)

                        menu.findItem(R.id.editor_delete_btn).isVisible = false
                    }
                    state.currentTags.forEach { addChip(getChip(it)) }

                    imm.showKeyboard()
                    binding.nameInput.requestFocus()
                }

                is EditorViewState.Editing -> {
                    binding.editorToolbar.apply {
                        title = getText(state.editorTitleId)

                        menu.findItem(R.id.editor_delete_btn).apply {
                            setOnMenuItemClickListener {
                                deleteLift(state)
                                true
                            }
                            isVisible = true
                        }
                    }
                    state.currentTags.forEach { addChip(getChip(it)) }
                }

                else -> {
                }
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
                editorViewModel.state.value?.removeCurrentTag(chipText)
                binding.chipGroup.removeView(it)
            }
        }
    }

    private fun addNewTag() {
        val tagName = binding.chipCreator.text.toString().trim()
        if (tagName.isNotEmpty() && editorViewModel.state.value?.addCurrentTag((tagName)) == true) {
            addChip(getChip(tagName))
        }
        binding.chipCreator.setText("")
    }

    private fun saveLift() {
        if (binding.nameInput.text?.trim().isNullOrEmpty()) {
            binding.nameInput.setText("")
            binding.nameInputLayout.isErrorEnabled = true
            binding.nameInputLayout.error = getString(R.string.empty_name_error_msg)
        } else {
            editorViewModel.state.value.run {
                when (this) {
                    is EditorViewState.Editing, is EditorViewState -> {
                        name = binding.nameInput.text.toString().trim()
                        tier = tierStrings.indexOf(binding.tierSelector.text.toString())
                        saveLift()
                        goBack()
                    }
                    else -> return
                }
            }
        }
    }

    private fun deleteLift(state: EditorViewState.Editing) {
        mainViewModel.deletedLift.value = state.lift
        mainViewModel.deletedLiftTags.value = state.oldTags
        state.deleteLift()
        goBack()
    }

    private fun goBack() {
        imm.hideKeyboard(requireView().windowToken)
        findNavController().popBackStack()
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