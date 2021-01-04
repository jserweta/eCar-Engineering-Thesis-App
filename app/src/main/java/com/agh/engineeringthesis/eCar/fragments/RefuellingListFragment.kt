package com.agh.engineeringthesis.eCar.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agh.engineeringthesis.eCar.activities.AddFuellingActivity
import com.agh.engineeringthesis.eCar.activities.MainActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.adapters.RefuellingListAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_refuelling_list.*

class RefuellingListFragment: Fragment() {

    private var dbHelper: VehicleDBHelper? = null
    private var currentVehicle: Vehicle? = null
    private var addFuellingFab: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_refuelling_list, container, false)
        dbHelper = VehicleDBHelper(activity!!.applicationContext, VehicleContract.tables)
        addFuellingFab = view.findViewById(R.id.add_fuelling_fab)
        val mainActivity: MainActivity? = activity as MainActivity?
        currentVehicle = mainActivity!!.getCurrentVehicle()

        addFuellingFab!!.setOnClickListener {
            run {
                val intent = Intent(activity, AddFuellingActivity::class.java)
                intent.putExtra("carId", currentVehicle!!.getId())
                startActivity(intent)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        refuellings_list.adapter = RefuellingListAdapter(getFuellings())
        refuellings_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        refuellings_list.setEmptyView(no_refuellings)
        refuellings_list.setHasFixedSize(true)
        super.onActivityCreated(savedInstanceState)
    }

    private fun getFuellings() =
        if (currentVehicle != null)
            dbHelper!!.getFuellingsForVehicle(currentVehicle!!.getId())
        else listOf()

}
