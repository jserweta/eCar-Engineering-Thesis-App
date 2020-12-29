package com.agh.engineeringthesis.eCar.logic

import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Fuelling
import com.agh.engineeringthesis.eCar.model.FuelStatistics
import org.joda.time.DateTime

data class RefuellingStatisticsGenerator(val dbHelper: VehicleDBHelper){

    private fun getCurrentMonthRefuellings(vehicleId: Int): List<Fuelling> {
        val allRefuellings: List<Fuelling> = getAllRefuellings(vehicleId)
        val monthBefore = DateTime().minusMonths(1).toDate()
        return allRefuellings.filter { fuelling -> fuelling.date.after(monthBefore) }
    }

    private fun getAllRefuellings(vehicleId: Int): List<Fuelling> = dbHelper.getFuellingsForVehicle(vehicleId)

    private fun getStatistics(refuellings: List<Fuelling>): FuelStatistics {
        var summaryConsumption = 0.0
        var summaryDistance = 0.0
        var summaryFuelCost = 0.0
        var minConsumption = 0.0
        var maxConsumption = 0.0
        var avgConsumption = 0.00
        var avgPricePerKilometer = 0.00
        val refuellingsNumber = refuellings.size
        var lastConsumption = 0.00
        var lastFuelPrice = 0.00
        var highestFuelPrice = 0.00
        var lowestFuelPrice = 100.00

        refuellings.forEach {
            summaryConsumption += it.fuelAmount
            summaryDistance += it.mileage - it.lastFuellingMileage
            summaryFuelCost += it.pricePerLitre * it.fuelAmount

            val consumption = it.fuelAmount / (it.mileage - it.lastFuellingMileage) * 100
            if ( consumption > maxConsumption) maxConsumption = consumption
            if ( minConsumption == 0.0 || minConsumption > consumption) minConsumption = consumption

            val fuelPrice = it.pricePerLitre
            if (fuelPrice > highestFuelPrice) highestFuelPrice = fuelPrice
            if (fuelPrice < lowestFuelPrice) lowestFuelPrice = fuelPrice
        }
        if (refuellings.isNotEmpty()){
            val newestRefuelling = refuellings.first()
            lastFuelPrice = newestRefuelling.pricePerLitre
            lastConsumption = newestRefuelling.fuelAmount / (newestRefuelling.mileage - newestRefuelling.lastFuellingMileage) * 100
            avgConsumption = summaryConsumption / summaryDistance  * 100
            avgPricePerKilometer = summaryFuelCost / summaryDistance
        }


        return FuelStatistics(
            summaryDistance = summaryDistance,
            summaryConsumption = summaryConsumption,
            avgConsumption = avgConsumption,
            avgPricePerKilometer = avgPricePerKilometer,
            summaryFuelCost = summaryFuelCost,
            minConsumption = minConsumption,
            maxConsumption = maxConsumption,
            refuellingsNumber = refuellingsNumber,
            lastConsumption = lastConsumption,
            lastFuelPrice = lastFuelPrice,
            highestFuelPrice = highestFuelPrice,
            lowestFuelPrice = lowestFuelPrice
        )
    }

    fun getStatisticsForCurrentMonth(vehicleId: Int) = getStatistics(getCurrentMonthRefuellings(vehicleId))
    fun getStatisticsForWholePeriod(vehicleId: Int) = getStatistics(getAllRefuellings(vehicleId))
}