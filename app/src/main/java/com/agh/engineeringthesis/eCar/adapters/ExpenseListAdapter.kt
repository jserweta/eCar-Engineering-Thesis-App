package com.agh.engineeringthesis.eCar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.model.Expense
import kotlinx.android.synthetic.main.expense_list_item.view.*

import java.text.SimpleDateFormat

class ExpenseListAdapter(var expenseList: List<Expense>): RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val expenseDateField: TextView = itemView.expense_date_field
        val expenseTypeField: TextView = itemView.expense_type_field
        val expenseNameField: TextView = itemView.expense_name_field
        val expenseCostField: TextView = itemView.expense_cost_field
        val expenseIcon: ImageView = itemView.expense_icon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.expense_list_item, parent, false)

        return ExpenseViewHolder(itemView)
    }

    override fun getItemCount() = expenseList.size

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentItem = expenseList[position]

        val formatter = SimpleDateFormat("YYYY-MM-dd")
        holder.expenseDateField.text = formatter.format(currentItem.date)
        holder.expenseTypeField.text = String.format("%s", currentItem.expenseType)
        holder.expenseNameField.text = String.format("%s", currentItem.costName)
        holder.expenseCostField.text = String.format("%.2f ", currentItem.costValue)
        holder.expenseIcon.setImageResource(setExpenseTypeIcon(currentItem))
    }

    private fun setExpenseTypeIcon(currentItem: Expense): Int {
        return when (currentItem.expenseType.toString()) {
            "Ubezpieczenie" -> {
                R.drawable.ic_car_insurance
            }
            "Mandat" -> {
                R.drawable.ic_local_policeman
            }
            "Przejazd" -> {
                R.drawable.ic_near_me_24
            }
            "Parking" -> {
                R.drawable.ic_parking_24
            }
            "Serwis" -> {
                R.drawable.ic_service_24
            }
            "Eksploatacja" -> {
                R.drawable.ic_settings_24
            }
            "Myjnia" -> {
                R.drawable.ic_car_wash_24
            }
            "Rejestracja" -> {
                R.drawable.ic_receipt_24
            }
            else -> R.drawable.ic_money_24
        }
    }
}