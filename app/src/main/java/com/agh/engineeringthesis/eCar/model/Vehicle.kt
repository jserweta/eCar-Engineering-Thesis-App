package com.agh.engineeringthesis.eCar.model

import android.os.Parcel
import android.os.Parcelable

data class Vehicle(private val id: Int, val name: String, val type: VehicleType, val mileage: Int): Entity, Parcelable {
    override fun getId(): Int {
        return id
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        VehicleType.valueOf(parcel.readString()!!),
        parcel.readInt()
    )

    enum class VehicleType {
        Samochód, Motocykl, Ciężarówka, Autobus
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(type.name)
        parcel.writeInt(mileage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vehicle> {
        override fun createFromParcel(parcel: Parcel): Vehicle {
            return Vehicle(parcel)
        }

        override fun newArray(size: Int): Array<Vehicle?> {
            return arrayOfNulls(size)
        }
    }
}
