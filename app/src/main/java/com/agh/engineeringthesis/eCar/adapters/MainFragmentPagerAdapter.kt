package com.agh.engineeringthesis.eCar.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.agh.engineeringthesis.eCar.fragments.ExpenseListFragment
import com.agh.engineeringthesis.eCar.fragments.MainPageFragment
import com.agh.engineeringthesis.eCar.fragments.RefuellingListFragment
import com.agh.engineeringthesis.eCar.fragments.VehicleListFragment

class MainFragmentPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainPageFragment()
            1 -> RefuellingListFragment()
            2 -> ExpenseListFragment()
            3 -> VehicleListFragment()
            else -> MainPageFragment()
        }
    }

    override fun getCount() = 4
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}