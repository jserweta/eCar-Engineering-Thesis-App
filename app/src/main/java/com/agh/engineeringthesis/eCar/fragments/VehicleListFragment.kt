package com.agh.engineeringthesis.eCar.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agh.engineeringthesis.eCar.AddVehicleActivity
import com.agh.engineeringthesis.eCar.MainActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.adapters.VehicleListAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_vehicle_manager.*
import com.agh.engineeringthesis.eCar.db.VehicleContract.VehicleEntry as Vehicles

class VehicleListFragment: Fragment() {

    private var addVehicleFab: FloatingActionButton? = null
    private var dbHelper: VehicleDBHelper? = null
    private var vehicleListAdapter: VehicleListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_vehicle_manager, container, false)
        addVehicleFab = view.findViewById(R.id.add_vehicle_fab)
        dbHelper = VehicleDBHelper(activity!!.applicationContext, VehicleContract.tables)

        addVehicleFab!!.setOnClickListener {
            val intent = Intent(activity, AddVehicleActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        vehicleListAdapter = VehicleListAdapter(getVehicles())
        vehicleListAdapter!!.setOnItemClickListener(object : VehicleListAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                val vehicleToRemove: Vehicle = vehicleListAdapter!!.getElementOnPosition(position)
                dbHelper!!.deleteById(Vehicles, vehicleToRemove.getId())
                dbHelper!!.deleteFuellingsForVehicle(vehicleToRemove.getId())
                dbHelper!!.deleteExpensesForVehicle(vehicleToRemove.getId())
                dbHelper!!.deleteNotificationsForVehicle(vehicleToRemove.getId())
                vehicleListAdapter!!.vehicleList = getVehicles()
                (activity as MainActivity).updateSpinner()
                vehicleListAdapter!!.notifyItemRemoved(position)
            }
        })
        vehicle_list.adapter = vehicleListAdapter
        vehicle_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        vehicle_list.setEmptyView(no_vehicles)
        vehicle_list.hasFixedSize()

        super.onActivityCreated(savedInstanceState)
    }

    fun updateVehicleList() {
        vehicleListAdapter!!.vehicleList = getVehicles()
        vehicleListAdapter!!.notifyDataSetChanged()
    }

    private fun getVehicles(): List<Vehicle> {
        return dbHelper!!.getAll(Vehicles)
    }
}
