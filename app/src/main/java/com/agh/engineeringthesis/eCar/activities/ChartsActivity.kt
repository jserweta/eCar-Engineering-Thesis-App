package com.agh.engineeringthesis.eCar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.adapters.ChartsTabPagerAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_charts.*


class ChartsActivity : AppCompatActivity() {

    var dbHelper: VehicleDBHelper? = null
    private var currentVehicle: Vehicle? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

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
        tab_charts_layout.addTab(tab_charts_layout.newTab().setText("Paliwo i inne wydatki"))
        tab_charts_layout.addTab(tab_charts_layout.newTab().setText("Wydatki (kategorie)"))
        tab_charts_layout.addTab(tab_charts_layout.newTab().setText("Ceny paliwa"))
        tab_charts_layout.addTab(tab_charts_layout.newTab().setText("Zużycie paliwa"))
        tab_charts_layout.addTab(tab_charts_layout.newTab().setText("Przebieg"))


        val chartsPagerAdapter = ChartsTabPagerAdapter(supportFragmentManager, tab_charts_layout.tabCount)
        chartsViewPager.adapter = chartsPagerAdapter
        chartsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_charts_layout))
        tab_charts_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                chartsViewPager.currentItem = tab.position
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

    fun getFuellings(activity: ChartsActivity) =
        if (currentVehicle != null)
            activity.dbHelper!!.getFuellingsForVehicle(currentVehicle!!.getId())
        else listOf()

    fun setCustomLegend(pieChart: PieChart, tableLayout: TableLayout, costs:ArrayList<PieEntry>) {
        val l = pieChart.legend
        val colorcodes: IntArray = getColors(l)!!
        val inflater = layoutInflater

        for (i in 0 until getColors(l)!!.size - 1) {
            val tableRow: TableRow = inflater.inflate(
                R.layout.legend_row,
                tableLayout, false
            ) as TableRow

            val color = tableRow.getChildAt(0) as ImageView
            val label = tableRow.getChildAt(1) as TextView
            val value = tableRow.getChildAt(2) as TextView

            color.setBackgroundColor(colorcodes[i])
            val labels = getLabels(l)
            label.text = labels!![i]
            value.text = String.format("%.2f zł", costs[i].value)
            tableLayout.addView(tableRow)
        }
        pieChart.legend.isWordWrapEnabled = true
        pieChart.legend.isEnabled = false
    }

    private fun getColors(legend: Legend): IntArray? {
        val legendEntries = legend.entries
        val colors = IntArray(legendEntries.size)
        for (i in legendEntries.indices) {
            colors[i] = legendEntries[i].formColor
        }
        return colors
    }

    private fun getLabels(legend: Legend): Array<String?>? {
        val legendEntries = legend.entries
        val labels = arrayOfNulls<String>(legendEntries.size)
        for (i in legendEntries.indices) {
            labels[i] = legendEntries[i].label
        }
        return labels
    }




}