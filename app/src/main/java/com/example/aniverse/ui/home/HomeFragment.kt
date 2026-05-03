package com.example.aniverse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aniverse.R
import com.example.aniverse.adapter.HomePagerAdapter
import com.example.aniverse.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
        setupBannerActions()
    }

    private fun setupBannerActions() {
        binding.btnBrowse.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        binding.btnSchedule.setOnClickListener {
            // Logic for schedule or navigate
            android.widget.Toast.makeText(requireContext(), getString(R.string.btn_schedule) + " Coming Soon", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTabLayout() {
        val tabs = listOf(getString(R.string.tab_top_anime), getString(R.string.tab_seasonal))
        val adapter = HomePagerAdapter(this, tabs.size)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}