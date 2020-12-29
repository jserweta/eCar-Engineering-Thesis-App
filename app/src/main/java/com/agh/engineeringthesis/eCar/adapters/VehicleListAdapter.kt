package com.agh.engineeringthesis.eCar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.model.Vehicle
import kotlinx.android.synthetic.main.vehicle_list_item.view.*


class VehicleListAdapter(var vehicleList:List<Vehicle>): RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class VehicleViewHolder(itemView: View, listener: OnItemClickListener?): RecyclerView.ViewHolder(itemView){

        val vehicleNameField:TextView = itemView.vehicle_name_field
        val vehicleTypeField:TextView = itemView.vehicle_type_field
        val vehicleMileage:TextView = itemView.mileage_field
        private val deleteBtn: ImageView = itemView.delete_vehicle

        init {
            deleteBtn.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position)
                    }
                }
            }
        }
    }

    override fun getItemCount() = vehicleList.size

    fun getElementOnPosition(position: Int): Vehicle = vehicleList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.vehicle_list_item, parent, false)
        return VehicleViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val currentItem = vehicleList[position]

        holder.vehicleNameField.text = currentItem.name
        holder.vehicleMileage.text = String.format("%d km", currentItem.mileage)
        holder.vehicleTypeField.text = currentItem.type.toString()

    }
}