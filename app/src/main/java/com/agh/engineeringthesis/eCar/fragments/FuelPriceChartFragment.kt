package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.agh.engineeringthesis.eCar.ChartsActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.util.CustomMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class FuelPriceChartFragment : Fragment() {

    private var fuelPriceLineChart: LineChart? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fuel_price_chart, container, false)
        fuelPriceLineChart = view.findViewById(R.id.fuel_price_line_chart)

        val activity: ChartsActivity = activity as ChartsActivity

        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)

        val fuellingList = activity.getFuellings(activity)
        val priceList = ArrayList<Entry>()

        for (i in fuellingList.indices){
            val consumptionValue = fuellingList[i].pricePerLitre
            priceList.add(Entry(i.toFloat(), consumptionValue.toFloat()))
        }

        val colorAcccent = context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        val colorPrimary = context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }

        val dataSet = LineDataSet(priceList, "")
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

        fuelPriceLineChart!!.setDrawMarkers(true)
        val markerView = CustomMarkerView(requireContext(), R.layout.marker_view, "cena paliwa")
        markerView.chartView = fuelPriceLineChart!!
        fuelPriceLineChart!!.marker= markerView
        fuelPriceLineChart!!.setTouchEnabled(true)
//        val labels = fuellingList.map { it.date}
//        fuelConsumptionLineChart!!.xAxis.valueFormatter = DateAxisValueFormatter(labels)


        val data = LineData(dataSet)

        fuelPriceLineChart!!.description.isEnabled = false
        fuelPriceLineChart!!.data = data
        fuelPriceLineChart!!.xAxis.setDrawGridLines(true)
        fuelPriceLineChart!!.xAxis.labelCount = 4
        fuelPriceLineChart!!.xAxis.granularity = 1f

        fuelPriceLineChart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fuelPriceLineChart!!.axisLeft.setDrawGridLines(true)
        fuelPriceLineChart!!.xAxis.axisLineColor = colorPrimary
        fuelPriceLineChart!!.xAxis.textColor = colorPrimary
        fuelPriceLineChart!!.axisLeft.axisLineColor = colorPrimary
        fuelPriceLineChart!!.axisLeft.textColor = colorPrimary
        fuelPriceLineChart!!.axisRight.axisLineColor = colorPrimary
        fuelPriceLineChart!!.axisRight.textColor = colorPrimary
        fuelPriceLineChart!!.setDrawBorders(false)
        fuelPriceLineChart!!.setDrawGridBackground(false)
        //fuelConsumptionLineChart!!.setDescriptionColor(Color.WHITE)
        fuelPriceLineChart!!.isAutoScaleMinMaxEnabled = false

        val rightAxis = fuelPriceLineChart!!.axisRight
        rightAxis.isEnabled = false

        val legend = fuelPriceLineChart!!.legend
        legend.isEnabled = false
        //linechart.setViewPortOffsets(0f, 0f, 0f, 0f) //remove padding
        fuelPriceLineChart!!.invalidate()
        fuelPriceLineChart!!.animateXY(300, 300)
        return view
    }

}