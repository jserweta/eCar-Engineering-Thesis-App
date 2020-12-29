package com.agh.engineeringthesis.eCar.model

data class Notification(
    private val id: Int = 0,
    val vehicleId: Int,
    val date: Long,
    val notificationName: String,
    val notificationType: NotificationType
) : Entity {
    override fun getId(): Int {
        return id
    }

    enum class NotificationType{
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
