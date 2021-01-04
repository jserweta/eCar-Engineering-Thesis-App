package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.agh.engineeringthesis.eCar.activities.ChartsActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.util.CustomMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import kotlin.collections.ArrayList


class FuelConsumptionChartFragment : Fragment() {
    private var fuelConsumptionLineChart: LineChart? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fuel_consumption_chart, container, false)

        fuelConsumptionLineChart = view.findViewById(R.id.fuel_consumption_line_chart)

        val activity: ChartsActivity = activity as ChartsActivity

        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)

        val fuellingList = activity.getFuellings(activity)
        val consumption = ArrayList<Entry>()

        for (i in fuellingList.indices){
            val consumptionValue = fuellingList[i].fuelAmount / (fuellingList[i].mileage - fuellingList[i].lastFuellingMileage) * 100
            consumption.add(Entry(i.toFloat(), consumptionValue.toFloat()))
        }

        val colorAcccent = context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        val colorPrimary = context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }

        val dataSet = LineDataSet(consumption, "")
        dataSet.setDrawFilled(false)
        dataSet.fillColor = colorPrimary!!
        dataSet.setDrawHighlightIndicators(true)
        dataSet.lineWidth = 1.5f
        dataSet.circleRadius = 5f
        dataSet.color = colorAcccent!!
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawCircles(false)
        dataSet.highLightColor = colorAcccent
        dataSet.valueTextSize = 14f
        dataSet.setDrawValues(false)

        fuelConsumptionLineChart!!.setDrawMarkers(true)
        val markerView = CustomMarkerView(requireContext(), R.layout.marker_view, "spalanie")
        markerView.chartView = fuelConsumptionLineChart!!
        fuelConsumptionLineChart!!.marker = markerView
        fuelConsumptionLineChart!!.setTouchEnabled(true)
//        val labels = fuellingList.map { it.date}
//        fuelConsumptionLineChart!!.xAxis.valueFormatter = DateAxisValueFormatter(labels)


        val data = LineData(dataSet)

        fuelConsumptionLineChart!!.description.isEnabled = false
        fuelConsumptionLineChart!!.data = data
        fuelConsumptionLineChart!!.xAxis.setDrawGridLines(true)
        fuelConsumptionLineChart!!.xAxis.granularity = 1f
        fuelConsumptionLineChart!!.xAxis.labelCount = 4

        fuelConsumptionLineChart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fuelConsumptionLineChart!!.axisLeft.setDrawGridLines(true)
        fuelConsumptionLineChart!!.xAxis.axisLineColor = colorPrimary
        fuelConsumptionLineChart!!.xAxis.textColor = colorPrimary
        fuelConsumptionLineChart!!.axisLeft.axisLineColor = colorPrimary
        fuelConsumptionLineChart!!.axisLeft.textColor = colorPrimary
        fuelConsumptionLineChart!!.axisRight.axisLineColor = colorPrimary
        fuelConsumptionLineChart!!.axisRight.textColor = colorPrimary
        fuelConsumptionLineChart!!.setDrawBorders(false)
        fuelConsumptionLineChart!!.setDrawGridBackground(false)
        //fuelConsumptionLineChart!!.setDescriptionColor(Color.WHITE)
        fuelConsumptionLineChart!!.isAutoScaleMinMaxEnabled = false

        val rightAxis = fuelConsumptionLineChart!!.axisRight
        rightAxis.isEnabled = false

        val legend = fuelConsumptionLineChart!!.legend
        legend.isEnabled = false
        //linechart.setViewPortOffsets(0f, 0f, 0f, 0f) //remove padding
        fuelConsumptionLineChart!!.invalidate()
        fuelConsumptionLineChart!!.animateXY(300, 300)


        return view
    }



}