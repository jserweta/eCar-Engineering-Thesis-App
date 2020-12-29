package com.agh.engineeringthesis.eCar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.model.Fuelling
import kotlinx.android.synthetic.main.fuelling_list_item.view.*

import java.text.SimpleDateFormat
import java.util.*

class RefuellingListAdapter(var refuellingList: List<Fuelling>): RecyclerView.Adapter<RefuellingListAdapter.RefuellingViewHolder>() {

    class RefuellingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateField: TextView = itemView.refuelling_date_field
        val priceField: TextView = itemView.refuelling_price_field
        val amountField: TextView = itemView.refuelling_amount_field
        val consumptionField: TextView = itemView.fuel_consumption
        val costField: TextView = itemView.refuelling_cost_field
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefuellingViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fuelling_list_item, parent, false)

        return RefuellingViewHolder(itemView)
    }

    override fun getItemCount() = refuellingList.size

    override fun onBindViewHolder(holder: RefuellingViewHolder, position: Int) {
        val currentItem = refuellingList[position]

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
        holder.dateField.text = formatter.format(currentItem.date)
        holder.priceField.text = String.format("%.2f zł/l (%s)", currentItem.pricePerLitre, currentItem.fuelType)
        holder.amountField.text = String.format("%.2f l", currentItem.fuelAmount)
        holder.consumptionField.text = String.format("%.2f ", consumption(currentItem))
        holder.costField.text = String.format("%.2f zł", cost(currentItem))
    }

    private fun consumption(currentItem: Fuelling): Double {
        return currentItem.fuelAmount / (currentItem.mileage - currentItem.lastFuellingMileage) * 100
    }
    private fun cost(currentItem: Fuelling): Double {
        return currentItem.pricePerLitre * currentItem.fuelAmount
    }
}