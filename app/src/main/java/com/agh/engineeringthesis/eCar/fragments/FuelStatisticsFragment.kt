package com.agh.engineeringthesis.eCar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.StatisticsActivity
import com.agh.engineeringthesis.eCar.logic.RefuellingStatisticsGenerator
import com.agh.engineeringthesis.eCar.model.Vehicle

class FuelStatisticsFragment : Fragment() {

    private var vehicleNameText: TextView? = null
    //private var vehicleMileageText: TextView? = null

    private var currentVehicle: Vehicle? = null

    private var vehicleSummaryDistanceM: TextView? = null
    private var vehicleSummaryConsumptionM: TextView? = null
    private var vehicleSummaryFuelCostM: TextView? = null
    private var vehicleFuellingsNumberM: TextView? = null
    private var avgConsumptionM: TextView? = null
    private var avgPricePerKmM: TextView? = null
    private var worstConsumptionM: TextView? = null
    private var bestConsumptionM: TextView? = null
    private var highestFuelPriceM: TextView? = null
    private var lowestFuelPriceM: TextView? = null

    private var vehicleSummaryDistanceA: TextView? = null
    private var vehicleSummaryConsumptionA: TextView? = null
    private var vehicleSummaryFuelCostA: TextView? = null
    private var vehicleFuellingsNumberA: TextView? = null
    private var avgConsumptionA: TextView? = null
    private var avgPricePerKmA: TextView? = null
    private var worstConsumptionA: TextView? = null
    private var bestConsumptionA: TextView? = null
    private var highestFuelPriceA: TextView? = null
    private var lowestFuelPriceA: TextView? = null


    private var refuellingStatisticsGenerator: RefuellingStatisticsGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fuel_statistics, container, false)

        vehicleNameText = view.findViewById(R.id.vehicle_name)
        //vehicleMileageText = view.findViewById(R.id.vehicle_mileage)

        vehicleSummaryDistanceM = view.findViewById(R.id.vehicle_distance)
        vehicleSummaryConsumptionM = view.findViewById(R.id.vehicle_consumption)
        vehicleSummaryFuelCostM = view.findViewById(R.id.vehicle_cost)
        vehicleFuellingsNumberM = view.findViewById(R.id.vehicle_fuellings_number)
        avgConsumptionM = view.findViewById(R.id.avg_consumption)
        avgPricePerKmM = view.findViewById(R.id.avg_price_perKm)
        worstConsumptionM = view.findViewById(R.id.worst_consumption)
        bestConsumptionM = view.findViewById(R.id.best_consumption)
        highestFuelPriceM = view.findViewById(R.id.highest_fuel_price)
        lowestFuelPriceM = view.findViewById(R.id.lowest_fuel_price)

        vehicleSummaryDistanceA = view.findViewById(R.id.vehicle_distance_all)
        vehicleSummaryConsumptionA = view.findViewById(R.id.vehicle_consumption_all)
        vehicleSummaryFuelCostA = view.findViewById(R.id.vehicle_cost_all)
        vehicleFuellingsNumberA = view.findViewById(R.id.vehicle_all_fuellings_number)
        avgConsumptionA = view.findViewById(R.id.avg_consumption_all)
        avgPricePerKmA = view.findViewById(R.id.avg_price_perKm_all)
        worstConsumptionA = view.findViewById(R.id.worst_consumption_all)
        bestConsumptionA = view.findViewById(R.id.best_consumption_all)
        highestFuelPriceA = view.findViewById(R.id.highest_fuel_price_all)
        lowestFuelPriceA = view.findViewById(R.id.lowest_fuel_price_all)


        val activity: StatisticsActivity? = activity as StatisticsActivity?
        currentVehicle = activity!!.getCurrentVehicle()
        refuellingStatisticsGenerator = RefuellingStatisticsGenerator(activity.dbHelper!!)

        displayVehicleData()

        return view

    }

    private fun displayVehicleData() {

        if (currentVehicle != null) {
            val wholePeriodStat = refuellingStatisticsGenerator!!.getStatisticsForWholePeriod(currentVehicle!!.getId())
            val currentMonthStat = refuellingStatisticsGenerator!!.getStatisticsForCurrentMonth(currentVehicle!!.getId())
            /*General info*/
            vehicleNameText!!.text = currentVehicle!!.name
            //vehicleMileageText!!.text = String.format("%d km",currentVehicle!!.mileage)
            /*Stat for current month*/
            vehicleSummaryDistanceM!!.text = String.format("%.0f km", currentMonthStat.summaryDistance)
            vehicleSummaryConsumptionM!!.text = String.format("%.2f l", currentMonthStat.summaryConsumption)
            vehicleSummaryFuelCostM!!.text = String.format("%.2f zł", currentMonthStat.summaryFuelCost)
            vehicleFuellingsNumberM!!.text = currentMonthStat.refuellingsNumber.toString()
            avgConsumptionM!!.text = String.format("%.2f l/100km", currentMonthStat.avgConsumption)
            avgPricePerKmM!!.text = String.format("%.2f zł/km", currentMonthStat.avgPricePerKilometer)
            worstConsumptionM!!.text = String.format("%.2f l/100km", currentMonthStat.maxConsumption)
            bestConsumptionM!!.text = String.format("%.2f l/100km", currentMonthStat.minConsumption)
            highestFuelPriceM!!.text = String.format("%.2f zł/l" , currentMonthStat.highestFuelPrice)
            lowestFuelPriceM!!.text = String.format("%.2f zł/l" , currentMonthStat.lowestFuelPrice)
            /*Stat for whole period*/
            vehicleSummaryDistanceA!!.text = String.format("%.0f km", wholePeriodStat.summaryDistance)
            vehicleSummaryConsumptionA!!.text = String.format("%.2f l", wholePeriodStat.summaryConsumption)
            vehicleSummaryFuelCostA!!.text = String.format("%.2f zł", wholePeriodStat.summaryFuelCost)
            vehicleFuellingsNumberA!!.text = wholePeriodStat.refuellingsNumber.toString()
            avgConsumptionA!!.text = String.format("%.2f l/100km", wholePeriodStat.avgConsumption)
            avgPricePerKmA!!.text = String.format("%.2f zł/km", wholePeriodStat.avgPricePerKilometer)
            worstConsumptionA!!.text = String.format("%.2f l/100km", wholePeriodStat.maxConsumption)
            bestConsumptionA!!.text = String.format("%.2f l/100km", wholePeriodStat.minConsumption)
            highestFuelPriceA!!.text = String.format("%.2f l/zł", wholePeriodStat.highestFuelPrice)
            lowestFuelPriceA!!.text = String.format("%.2f l/zł", wholePeriodStat.lowestFuelPrice)

        }
    }

}