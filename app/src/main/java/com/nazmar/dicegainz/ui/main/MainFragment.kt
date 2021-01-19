package com.nazmar.dicegainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.MainFragmentBinding
import com.nazmar.dicegainz.ui.SectionsPagerAdapter


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            requireNotNull(
                this.activity
            ).application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MainFragmentBinding.inflate(inflater)

        // Show undo snackbar for deleted lift
        viewModel.deletedLift.observe(viewLifecycleOwner) { deletedLift ->
            deletedLift?.let {
                Snackbar.make(binding.root, getString(R.string.lift_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) { viewModel.restoreDeletedLift() }
                    .setAnchorView(binding.fab)
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
                                getDrawable(
                                    requireContext(),
                                    R.drawable.ic_baseline_casino_24
                                )
                            )
                            setOnClickListener {
                                viewModel.rollAll()
                            }
                            viewModel.liftsLoaded.observe(viewLifecycleOwner) { liftsLoaded ->
                                visibility = if (liftsLoaded) {
                                    View.VISIBLE
                                } else {
                                    View.GONE
                                }
                            }
                        }
                        1 -> binding.fab.apply {
                            setImageDrawable(
                                getDrawable(
                                    requireContext(),
                                    R.drawable.ic_baseline_add_24
                                )
                            )
                            setOnClickListener {
                                it?.apply {
                                    isEnabled = false
                                    postDelayed({ isEnabled = true }, 400)
                                } //400 ms
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
            tab.text = if (position == 0)
                getString(R.string.tab_text_1)
            else
                getString(R.string.tab_text_2)
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
        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)
        val action = MainFragmentDirections.actionMainFragmentToEditorFragment(null)
        navController.navigate(action)
    }

    private fun showThemeDialog(): Boolean {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToThemeDialogFragment())
        return true
    }

    private fun showAboutDialog(): Boolean {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToAboutDialog())
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}