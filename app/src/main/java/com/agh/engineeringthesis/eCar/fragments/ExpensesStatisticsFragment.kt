package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.activities.StatisticsActivity
import com.agh.engineeringthesis.eCar.logic.ExpensesStatisticsGenerator
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.model.Vehicle

class ExpensesStatisticsFragment : Fragment() {

    private var vehicleNameText: TextView? = null

    private var currentVehicle: Vehicle? = null
    private var expensesStatisticsGenerator: ExpensesStatisticsGenerator? = null
    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null

    private var totalExpenses: TextView? = null
    private var fuelExpenses: TextView? = null
    private var categorizedExpenses: TextView? = null

    private var insuranceExpenses: TextView? = null
    private var ticketExpenses: TextView? = null
    private var parkingExpenses: TextView? = null
    private var serviceExpenses: TextView? = null
    private var exploitationExpenses: TextView? = null
    private var carWashExpenses: TextView? = null
    private var roadExpenses: TextView? = null
    private var registrationExpenses: TextView? = null
    private var otherExpenses: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expenses_statistics, container, false)

        vehicleNameText = view.findViewById(R.id.vehicle_name)
        totalExpenses = view.findViewById(R.id.total_expenses_value)
        fuelExpenses = view.findViewById(R.id.fuel_expenses_value)
        categorizedExpenses = view.findViewById(R.id.categorized_expenses_value)

        insuranceExpenses = view.findViewById(R.id.insurance_expenses_value)
        ticketExpenses = view.findViewById(R.id.tickets_expense_value)
        parkingExpenses = view.findViewById(R.id.parking_expenses_value)
        serviceExpenses = view.findViewById(R.id.service_expenses_value)
        exploitationExpenses = view.findViewById(R.id.exploitation_expenses_value)
        carWashExpenses = view.findViewById(R.id.car_wash_expenses_value)
        roadExpenses = view.findViewById(R.id.road_expenses_value)
        registrationExpenses = view.findViewById(R.id.registration_expenses_value)
        otherExpenses = view.findViewById(R.id.other_expenses_value)

        val activity: StatisticsActivity? = activity as StatisticsActivity?
        currentVehicle = activity!!.getCurrentVehicle()
        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)
        expensesStatisticsGenerator = ExpensesStatisticsGenerator(activity.dbHelper!!)


       displayExpensesData()
        return view
    }

    private fun displayExpensesData() {

        if (currentVehicle != null) {
            val fuelAllStats = refuellingStatisticsGenerator!!.getStatisticsForWholePeriod(currentVehicle!!.getId())
            val expensesStats = expensesStatisticsGenerator!!.getExpensesStatistics(currentVehicle!!.getId())

            vehicleNameText!!.text = currentVehicle!!.name
            totalExpenses!!.text = String.format("%.2f zł", fuelAllStats.summaryFuelCost + expensesStats.summaryCategorizedCosts)
            fuelExpenses!!.text = String.format("%.2f zł", fuelAllStats.summaryFuelCost)
            categorizedExpenses!!.text = String.format("%.2f zł", expensesStats.summaryCategorizedCosts)

            insuranceExpenses!!.text = String.format("%.2f zł", expensesStats.summaryInsuranceCosts)
            ticketExpenses!!.text = String.format("%.2f zł", expensesStats.summaryTrafficOffenceCosts)
            parkingExpenses!!.text = String.format("%.2f zł", expensesStats.summaryParkingCosts)
            serviceExpenses!!.text = String.format("%.2f zł", expensesStats.summaryRepairCosts)
            exploitationExpenses!!.text = String.format("%.2f zł", expensesStats.summaryExploitationCosts)
            carWashExpenses!!.text = String.format("%.2f zł", expensesStats.summaryCarWashCosts)
            roadExpenses!!.text = String.format("%.2f zł", expensesStats.summaryRoadFeeCosts)
            registrationExpenses!!.text = String.format("%.2f zł", expensesStats.summaryRegistrationCosts)
            otherExpenses!!.text = String.format("%.2f zł", expensesStats.summaryOtherCosts)

        }

    }

}