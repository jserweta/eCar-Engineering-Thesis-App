package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.agh.engineeringthesis.eCar.activities.MainActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.logic.ExpensesStatisticsGenerator
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.model.Vehicle

class MainPageFragment: Fragment() {

    private var vehicleNameText: TextView? = null
    private var vehicleMileageText: TextView? = null
    private var vehicleTypeText: TextView? = null

    private var avgConsumptionA: TextView? = null
    private var lastConsumption: TextView? = null
    private var lastFuelPrice: TextView? = null

    private var vehicleSummaryFuelCostA: TextView? = null
    private var vehicleOtherCostA: TextView? = null

    private var currentVehicle: Vehicle? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null
    private var expensesStatisticsGenerator: ExpensesStatisticsGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main_page, container, false)

        vehicleNameText = view.findViewById(R.id.vehicle_name)
        vehicleMileageText = view.findViewById(R.id.vehicle_mileage)
        vehicleTypeText = view.findViewById(R.id.vehicle_type)

        avgConsumptionA = view.findViewById(R.id.avg_consumption_all)
        lastFuelPrice = view.findViewById(R.id.last_fuel_price_text)
        lastConsumption = view.findViewById(R.id.last_consum_text)

        vehicleSummaryFuelCostA = view.findViewById(R.id.summary_fuel_cost)
        vehicleOtherCostA = view.findViewById(R.id.other_costs)


        val activity: MainActivity? = activity as MainActivity?
        currentVehicle = activity!!.getCurrentVehicle()
        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)
        expensesStatisticsGenerator = ExpensesStatisticsGenerator(activity.dbHelper!!)

        displayVehicleData()


        return view
    }

    private fun displayVehicleData() {

        if (currentVehicle != null) {
            val wholePeriodStat = refuellingStatisticsGenerator!!.getStatisticsForWholePeriod(currentVehicle!!.getId())
            /*General info*/
            vehicleNameText!!.text = currentVehicle!!.name
            vehicleMileageText!!.text = String.format("%d km",currentVehicle!!.mileage)
            vehicleTypeText!!.text = currentVehicle!!.type.toString()

            /*Stat fuellings*/
            avgConsumptionA!!.text = String.format("%.2f ", wholePeriodStat.avgConsumption)
            lastConsumption!!.text = String.format("%.2f ", wholePeriodStat.lastConsumption)
            lastFuelPrice!!.text = String.format("%.2f ", wholePeriodStat.lastFuelPrice)
            /* Cost stats*/
            vehicleSummaryFuelCostA!!.text = String.format("%.2f ", wholePeriodStat.summaryFuelCost)
            vehicleOtherCostA!!.text = String.format("%.2f ", expensesStatisticsGenerator!!.getExpensesStatistics(currentVehicle!!.getId()).summaryCategorizedCosts)

        }
    }
}
