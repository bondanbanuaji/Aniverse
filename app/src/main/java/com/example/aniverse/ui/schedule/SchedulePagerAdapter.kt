package com.example.aniverse.ui.schedule

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SchedulePagerAdapter(fragment: Fragment, private val days: List<String>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = days.size

    override fun createFragment(position: Int): Fragment {
        return DayScheduleFragment.newInstance(days[position])
    }
}