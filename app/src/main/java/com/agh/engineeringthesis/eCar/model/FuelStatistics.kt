package com.agh.engineeringthesis.eCar.model

data class FuelStatistics(
    val summaryDistance: Double,
    val summaryConsumption: Double,
    val avgConsumption: Double,
    val avgPricePerKilometer: Double,
    val summaryFuelCost: Double,
    val minConsumption: Double,
    val maxConsumption: Double,
    val refuellingsNumber: Int,
    val lastConsumption: Double,
    val lastFuelPrice: Double,
    val highestFuelPrice: Double,
    val lowestFuelPrice: Double
)