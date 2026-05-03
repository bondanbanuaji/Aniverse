package com.example.aniverse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
        loadBanner()
    }

    private fun loadBanner() {
        Glide.with(this)
            .load(R.drawable.banner_gojo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .into(binding.ivBanner)
    }

    private fun setupBannerActions() {
        binding.btnBrowse.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        binding.btnSchedule.setOnClickListener {
            findNavController().navigate(R.id.scheduleFragment)
        }
    }

    private fun setupTabLayout() {
        val tabs = listOf(
            getString(R.string.tab_popular),
            getString(R.string.tab_top_anime),
            getString(R.string.tab_seasonal),
            getString(R.string.tab_upcoming)
        )
        val adapter = HomePagerAdapter(this, tabs.size)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()

        // Update Title dynamically when tab changes
        binding.viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvSectionTitle.text = tabs[position]
            }
        })

        // Make "See All" functional
        binding.btnSeeAll.setOnClickListener {
            val currentTab = binding.tabLayout.selectedTabPosition
            val query = when (currentTab) {
                0 -> "Trending"
                1 -> "Top Anime"
                2 -> "Seasonal"
                3 -> "Upcoming"
                else -> ""
            }
            Toast.makeText(requireContext(), "Showing all $query", Toast.LENGTH_SHORT).show()
            // Optional: navigate to a dedicated "View All" or search fragment
            // findNavController().navigate(R.id.searchFragment) 
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}