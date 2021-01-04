package com.agh.engineeringthesis.eCar.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.fragment.app.Fragment
import com.agh.engineeringthesis.eCar.activities.ChartsActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.ExpensesStatisticsGenerator
import com.agh.engineeringthesis.eCar.model.Vehicle

import com.github.mikephil.charting.charts.PieChart

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry



class ExpensesCategorizedChartFragment : Fragment() {
    private var currentVehicle: Vehicle? = null
    private var expensesStatisticsGenerator: ExpensesStatisticsGenerator? = null
    private var expensesCatPieChart: PieChart? = null

    private var tableLayout: TableLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expenses_categorized_chart, container, false)

        expensesCatPieChart = view.findViewById(R.id.expenses_cat_split_pie_chart)

        val activity: ChartsActivity? = activity as ChartsActivity?
        currentVehicle = activity!!.getCurrentVehicle()
        expensesStatisticsGenerator = ExpensesStatisticsGenerator(activity.dbHelper!!)

        val catStats = expensesStatisticsGenerator!!.getExpensesStatistics(currentVehicle!!.getId())

        val costs = ArrayList<PieEntry>()
        costs.add(PieEntry(catStats.summaryCarWashCosts.toFloat(), "Myjnia"))
        costs.add(PieEntry(catStats.summaryInsuranceCosts.toFloat(), "Ubezpieczenie"))
        costs.add(PieEntry(catStats.summaryRoadFeeCosts.toFloat(), "Przejazd"))
        costs.add(PieEntry(catStats.summaryTrafficOffenceCosts.toFloat(), "Mandat"))
        costs.add(PieEntry(catStats.summaryParkingCosts.toFloat(), "Parking"))
        costs.add(PieEntry(catStats.summaryRepairCosts.toFloat(), "Serwis"))
        costs.add(PieEntry(catStats.summaryExploitationCosts.toFloat(), "Eksploatacja"))
        costs.add(PieEntry(catStats.summaryRegistrationCosts.toFloat(), "Rejestracja"))
        costs.add(PieEntry(catStats.summaryOtherCosts.toFloat(), "Inny"))

        val colorsList = listOf(
            Color.parseColor("#003f5c"),
            Color.parseColor("#2f4b7c"),
            Color.parseColor("#665191"),
            Color.parseColor("#a05195"),
            Color.parseColor("#d45087"),
            Color.parseColor("#f95d6a"),
            Color.parseColor("#ff7c43"),
            Color.parseColor("#ffa600"),
            Color.parseColor("#3d708f")
        )

        expensesCatPieChart!!.setExtraOffsets(30f, 0f, 30f, 0f)

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
        expensesCatPieChart!!.data = pieData
        expensesCatPieChart!!.description.isEnabled = false
        expensesCatPieChart!!.setDrawEntryLabels(false)

        tableLayout = view.findViewById(R.id.table_layout)
        activity.setCustomLegend(expensesCatPieChart!!, tableLayout!!, costs)

        expensesCatPieChart!!.isDrawHoleEnabled = true
        expensesCatPieChart!!.holeRadius = 45f

        expensesCatPieChart!!.animate()

        return view
    }




}
