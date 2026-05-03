package com.example.aniverse.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aniverse.databinding.FragmentScheduleBinding
import com.google.android.material.tabs.TabLayoutMediator

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    private val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SchedulePagerAdapter(this, days)
        binding.viewPagerSchedule.adapter = adapter

        TabLayoutMediator(binding.tabLayoutSchedule, binding.viewPagerSchedule) { tab, position ->
            tab.text = dayLabels[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}