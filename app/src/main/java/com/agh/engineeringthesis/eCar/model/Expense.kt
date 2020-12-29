package com.agh.engineeringthesis.eCar.model

import java.util.*

data class Expense(
    private val id: Int = 0,
    val vehicleId: Int,
    val date: Date = Date(),
    val costName: String,
    val costValue: Double,
    val expenseType: ExpenseType
) : Entity {
    override fun getId(): Int {
        return id
    }

    enum class ExpenseType{
        Ubezpieczenie,
        Mandat,
        Przejazd,
        Parking,
        Serwis,
        Eksploatacja,
        Myjnia,
        Rejestracja,
        Inny
    }
}