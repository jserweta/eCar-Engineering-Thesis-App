package com.agh.engineeringthesis.eCar.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.agh.engineeringthesis.eCar.fragments.*


class ChartsTabPagerAdapter(fragmentManager: FragmentManager, private var tabCount: Int): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FuelExpensesSplitChartFragment()
            1 -> ExpensesCategorizedChartFragment()
            2 -> FuelPriceChartFragment()
            3 -> FuelConsumptionChartFragment()
            4 -> MileageChartFragment()
            else -> FuelExpensesSplitChartFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}