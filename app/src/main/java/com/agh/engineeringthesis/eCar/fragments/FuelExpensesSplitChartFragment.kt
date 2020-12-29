package com.agh.engineeringthesis.eCar.fragments


import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.core.content.ContextCompat
import com.agh.engineeringthesis.eCar.ChartsActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.ExpensesStatisticsGenerator
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.model.Vehicle

import com.github.mikephil.charting.charts.PieChart

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class FuelExpensesSplitChartFragment : Fragment() {

    private var currentVehicle: Vehicle? = null
    private var fuelExpensesSplitPieChart: PieChart? = null
    private var expensesStatisticsGenerator: ExpensesStatisticsGenerator? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null
    private var tableLayout: TableLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_fuel_expenses_split_chart, container, false)

        fuelExpensesSplitPieChart = view.findViewById(R.id.fuel_expenses_split_pie_chart)

        val activity: ChartsActivity = activity as ChartsActivity
        currentVehicle = activity.getCurrentVehicle()
        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)
        expensesStatisticsGenerator = ExpensesStatisticsGenerator(activity.dbHelper!!)

        val costs = ArrayList<PieEntry>()
        costs.add(PieEntry(refuellingStatisticsGenerator!!.getStatisticsForWholePeriod(currentVehicle!!.getId()).summaryFuelCost.toFloat(), "Paliwo"))
        costs.add(PieEntry(expensesStatisticsGenerator!!.getExpensesStatistics(currentVehicle!!.getId()).summaryCategorizedCosts.toFloat(), "Inne wydatki"))

        val colorFirst = context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        val colorSecond = context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }
        val colorsList = mutableListOf(colorFirst, colorSecond)

        fuelExpensesSplitPieChart!!.setExtraOffsets(30f, 0f, 30f, 0f)

        val pieDataSet = PieDataSet(costs, "")
        pieDataSet.colors = colorsList
        pieDataSet.setValueTextColors(colorsList)

        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.valueTextSize = 16f
        pieDataSet.valueLineWidth = 1.5f
        pieDataSet.isUsingSliceColorAsValueLineColor = true
        pieDataSet.valueLinePart1Length = 0.6f
        pieDataSet.valueLinePart2Length = 0.3f
        pieDataSet.valueTypeface = Typeface.DEFAULT_BOLD
        pieDataSet.valueLinePart1OffsetPercentage = 120f

        val pieData = PieData(pieDataSet)
        fuelExpensesSplitPieChart!!.data = pieData
        fuelExpensesSplitPieChart!!.description.isEnabled = false
        fuelExpensesSplitPieChart!!.setDrawEntryLabels(false)

        tableLayout = view.findViewById(R.id.table_layout)
        activity.setCustomLegend(fuelExpensesSplitPieChart!!, tableLayout!!, costs)

        fuelExpensesSplitPieChart!!.isDrawHoleEnabled = true
        fuelExpensesSplitPieChart!!.holeRadius = 45f

        fuelExpensesSplitPieChart!!.animate()


        return view
    }

}