package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.agh.engineeringthesis.eCar.activities.ChartsActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.util.CustomMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MileageChartFragment : Fragment() {
    private var mileageLineChart: LineChart? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_mileage_chart, container, false)
        mileageLineChart = view.findViewById(R.id.mileageLineChart)

        val activity: ChartsActivity = activity as ChartsActivity

        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)

        val fuellingList = activity.getFuellings(activity).reversed()
        val priceList = ArrayList<Entry>()

        for (i in fuellingList.indices ){
            val mileage = fuellingList[i].mileage
            priceList.add(Entry(i.toFloat(), mileage.toFloat()))
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

        mileageLineChart!!.setDrawMarkers(true)
        val markerView = CustomMarkerView(requireContext(), R.layout.marker_view, "przebieg")
        markerView.chartView = mileageLineChart!!
        mileageLineChart!!.marker = markerView
        mileageLineChart!!.setTouchEnabled(true)
//        val labels = fuellingList.map { it.date}
//        fuelConsumptionLineChart!!.xAxis.valueFormatter = DateAxisValueFormatter(labels)


        val data = LineData(dataSet)

        mileageLineChart!!.description.isEnabled = false
        mileageLineChart!!.data = data
        mileageLineChart!!.xAxis.setDrawGridLines(true)

        mileageLineChart!!.xAxis.granularity = 1f
        mileageLineChart!!.xAxis.labelCount = 4
        mileageLineChart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mileageLineChart!!.axisLeft.setDrawGridLines(true)
        mileageLineChart!!.xAxis.axisLineColor = colorPrimary
        mileageLineChart!!.xAxis.textColor = colorPrimary
        mileageLineChart!!.axisLeft.axisLineColor = colorPrimary
        mileageLineChart!!.axisLeft.textColor = colorPrimary
        mileageLineChart!!.axisRight.axisLineColor = colorPrimary
        mileageLineChart!!.axisRight.textColor = colorPrimary
        mileageLineChart!!.setDrawBorders(false)
        mileageLineChart!!.setDrawGridBackground(false)
        //fuelConsumptionLineChart!!.setDescriptionColor(Color.WHITE)
        mileageLineChart!!.isAutoScaleMinMaxEnabled = false

        val rightAxis = mileageLineChart!!.axisRight
        rightAxis.isEnabled = false

        val legend = mileageLineChart!!.legend
        legend.isEnabled = false
        //linechart.setViewPortOffsets(0f, 0f, 0f, 0f) //remove padding
        mileageLineChart!!.invalidate()
        mileageLineChart!!.animateXY(300, 300)
        return view
    }
}