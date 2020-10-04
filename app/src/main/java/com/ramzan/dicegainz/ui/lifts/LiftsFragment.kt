package com.ramzan.dicegainz.ui.lifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.databinding.LiftsFragmentBinding
import com.ramzan.dicegainz.ui.editor.EditorFragment
import com.ramzan.dicegainz.ui.main.LIFTS_FILTER_ID
import com.ramzan.dicegainz.ui.main.MainViewModel
import com.ramzan.dicegainz.ui.main.MainViewModelFactory

class LiftsFragment : Fragment(), EditorFragment.EditorDialogListener {

    private lateinit var binding: LiftsFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.lifts_fragment, container, false
        )

        // Get ViewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        ).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        binding.apply {

            // Set up filter spinner
            viewModel.tagList.observe(viewLifecycleOwner, {
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(requireContext(), R.layout.tier_list_item, it)
                filterBar.setAdapter(adapter)
                if (filterBar.text.isNullOrEmpty()) filterBar.setText(
                    getString(R.string.all),
                    false
                )
            })
            filterBar.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                viewModel.updateFilterText(LIFTS_FILTER_ID, filterBar.text.toString())
            }

            // Set the recyclerview adapter
            val adapter = LiftAdapter(LiftAdapter.OnClickListener {
                showEditDialog(it)
            })

            liftList.adapter = adapter

            viewModel.lifts.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })

            fab.setOnClickListener {
                showEditDialog(null)
            }

            return root
        }
    }

    private fun showEditDialog(lift: Lift?) {
        val fm = parentFragmentManager
        val editorDialogFragment = EditorFragment.newInstance(lift)
        editorDialogFragment.setTargetFragment(this, 300)
        editorDialogFragment.show(fm, tag)
    }

    override fun onFinishEditing(deletedLift: Lift?, deletedTags: List<String>?) {
        // Show undo snackbar for deleted lift
        if (deletedLift != null && deletedTags != null) (
                Snackbar.make(binding.root, getString(R.string.lift_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        @Suppress("UNCHECKED_CAST")
                        viewModel.addLift(deletedLift, deletedTags)
                    }
                    .setAnchorView(binding.fab)
                    .show()
                )
    }

    companion object {
        @JvmStatic
        fun newInstance(): LiftsFragment = LiftsFragment()
    }
}
