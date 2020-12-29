package com.agh.engineeringthesis.eCar.model

import java.util.*

data class Fuelling(
    private val id: Int = 0,
    val vehicleId: Int,
    val date: Date = Date(),
    val fuelAmount: Double,
    val pricePerLitre: Double,
    val fuelType: FuelType,
    val lastFuellingMileage: Int,
    val mileage: Int
) : Entity {
    override fun getId(): Int {
        return id
    }

    enum class FuelType {
       Pb95, Pb98, ON, LPG
    }
}