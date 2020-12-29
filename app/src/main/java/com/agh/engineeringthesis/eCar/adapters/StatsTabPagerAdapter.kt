package com.agh.engineeringthesis.eCar.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.agh.engineeringthesis.eCar.fragments.ExpensesStatisticsFragment
import com.agh.engineeringthesis.eCar.fragments.FuelStatisticsFragment


class StatsTabPagerAdapter(fragmentManager: FragmentManager, private var tabCount: Int): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FuelStatisticsFragment()
            1 -> ExpensesStatisticsFragment()
            else -> FuelStatisticsFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}