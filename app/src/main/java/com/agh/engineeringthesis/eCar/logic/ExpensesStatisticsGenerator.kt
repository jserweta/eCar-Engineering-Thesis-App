package com.agh.engineeringthesis.eCar.logic

import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Expense
import com.agh.engineeringthesis.eCar.model.ExpenseStatistics

class ExpensesStatisticsGenerator(val dbHelper: VehicleDBHelper) {
    private fun getAllExpenses(vehicleId: Int): List<Expense> = dbHelper.getExpensesForVehicle(vehicleId)

    private fun getStatistics(expenses: List<Expense>): ExpenseStatistics {
        var summaryCategorizedCosts = 0.00
        var summaryInsuranceCosts = 0.00
        var summaryTrafficOffenceCosts = 0.00
        var summaryParkingCosts = 0.00
        var summaryRepairCosts = 0.00
        var summaryExploitationCosts = 0.00
        var summaryCarWashCosts = 0.00
        var summaryRoadFeeCosts = 0.00
        var summaryRegistrationCosts = 0.00
        var summaryOtherCosts = 0.00

        expenses.forEach { it ->
            summaryCategorizedCosts += it.costValue
            when (it.expenseType.toString()) {
                "Ubezpieczenie" -> {
                    summaryInsuranceCosts += it.costValue
                }
                "Mandat" -> {
                    summaryTrafficOffenceCosts += it.costValue
                }
                "Przejazd" -> {
                    summaryParkingCosts += it.costValue
                }
                "Parking" -> {
                    summaryRepairCosts += it.costValue
                }
                "Serwis" -> {
                    summaryExploitationCosts += it.costValue
                }
                "Eksploatacja" -> {
                    summaryCarWashCosts += it.costValue
                }
                "Myjnia" -> {
                    summaryRoadFeeCosts += it.costValue
                }
                "Rejestracja" -> {
                    summaryRegistrationCosts += it.costValue
                }
                else -> summaryOtherCosts += it.costValue
            }
        }

        return ExpenseStatistics(
            summaryCategorizedCosts = summaryCategorizedCosts,
            summaryInsuranceCosts = summaryInsuranceCosts,
            summaryTrafficOffenceCosts = summaryTrafficOffenceCosts,
            summaryParkingCosts = summaryParkingCosts,
            summaryRepairCosts = summaryRepairCosts,
            summaryExploitationCosts = summaryExploitationCosts,
            summaryCarWashCosts = summaryCarWashCosts,
            summaryRoadFeeCosts = summaryRoadFeeCosts,
            summaryRegistrationCosts = summaryRegistrationCosts,
            summaryOtherCosts = summaryOtherCosts
        )
    }
    fun getExpensesStatistics(vehicleId: Int) = getStatistics(getAllExpenses(vehicleId))

}