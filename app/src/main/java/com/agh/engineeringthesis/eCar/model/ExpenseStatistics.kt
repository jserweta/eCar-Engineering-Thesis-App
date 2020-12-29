package com.agh.engineeringthesis.eCar.model

data class ExpenseStatistics(
    val summaryCategorizedCosts: Double,
    val summaryInsuranceCosts: Double,
    val summaryTrafficOffenceCosts: Double,
    val summaryParkingCosts: Double,
    val summaryRepairCosts: Double,
    val summaryExploitationCosts: Double,
    val summaryCarWashCosts: Double,
    val summaryRoadFeeCosts: Double,
    val summaryRegistrationCosts: Double,
    val summaryOtherCosts: Double
)
