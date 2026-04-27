package com.example.aniverse.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aniverse.ui.home.AnimeListFragment

class HomePagerAdapter(fragment: Fragment, private val tabCount: Int)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount() = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnimeListFragment.newInstance("top")
            1 -> AnimeListFragment.newInstance("seasonal")
            else -> AnimeListFragment.newInstance("top")
        }
    }
}