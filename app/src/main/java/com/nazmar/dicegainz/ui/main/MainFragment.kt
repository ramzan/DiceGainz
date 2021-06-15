package com.nazmar.dicegainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.MainFragmentBinding
import com.nazmar.dicegainz.ui.BaseFragment


class MainFragment : BaseFragment<MainFragmentBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpBinding(MainFragmentBinding.inflate(inflater))

        // Show undo snackbar for deleted lift
        viewModel.deletedLift.observe(viewLifecycleOwner) { deletedLift ->
            deletedLift?.let {
                Snackbar.make(binding.root, getString(R.string.lift_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) { viewModel.restoreDeletedLift() }
                    .setAnchorView(binding.fab)
                    .addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            viewModel.clearDeletedLift()
                        }
                    })
                    .show()
            }
        }

        binding.viewPager.apply {
            adapter = SectionsPagerAdapter(this@MainFragment)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> binding.fab.apply {
                            setImageDrawable(
                                getDrawable(requireContext(), R.drawable.ic_baseline_casino_24)
                            )
                            setOnClickListener {
                                viewModel.rollAll()
                            }
                        }
                        1 -> binding.fab.apply {
                            setImageDrawable(
                                getDrawable(requireContext(), R.drawable.ic_round_add_24)
                            )
                            setOnClickListener {
                                showEditDialog()
                            }
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> binding.fab.show()
                        ViewPager2.SCROLL_STATE_SETTLING -> binding.fab.hide()
                        ViewPager2.SCROLL_STATE_DRAGGING -> {
                        }
                    }
                }
            })
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text =
                if (position == 0) getString(R.string.tab_text_1) else getString(R.string.tab_text_2)
        }.attach()

        binding.mainToolbar.setOnMenuItemClickListener {
            when (it) {
                binding.mainToolbar.menu[0] -> showThemeDialog()
                binding.mainToolbar.menu[1] -> showAboutDialog()
                else -> false
            }
        }

        return binding.root
    }

    private fun showEditDialog() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToEditorFragment())
    }

    private fun showThemeDialog(): Boolean {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToThemeDialogFragment())
        return true
    }

    private fun showAboutDialog(): Boolean {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToAboutDialog())
        return true
    }
}