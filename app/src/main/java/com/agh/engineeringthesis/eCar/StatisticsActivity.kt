package com.agh.engineeringthesis.eCar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.adapters.StatsTabPagerAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_statistics.*

class StatisticsActivity : AppCompatActivity() {

    var dbHelper: VehicleDBHelper? = null
    private var currentVehicle: Vehicle? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        dbHelper = VehicleDBHelper(this, VehicleContract.tables)
        val currentVehicleId = intent.getIntExtra("vehicleID", 0)
        currentVehicle = dbHelper!!.getById(VehicleContract.VehicleEntry, currentVehicleId)

        configureTabLayout()

    }

    private fun configureTabLayout() {
        tab_stats_layout.addTab(tab_stats_layout.newTab().setText("Paliwo"))
        tab_stats_layout.addTab(tab_stats_layout.newTab().setText("Wydatki"))

        val statsPagerAdapter = StatsTabPagerAdapter(supportFragmentManager, tab_stats_layout.tabCount)
        statsViewPager.adapter = statsPagerAdapter
        statsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_stats_layout))
        tab_stats_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                statsViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getCurrentVehicle(): Vehicle? {
        return currentVehicle
    }
}