package com.agh.engineeringthesis.eCar.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agh.engineeringthesis.eCar.AddExpenseActivity
import com.agh.engineeringthesis.eCar.MainActivity
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.adapters.ExpenseListAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_expense_list.*

class ExpenseListFragment : Fragment() {

    private var dbHelper: VehicleDBHelper? = null
    private var currentVehicle: Vehicle? = null
    private var addExpenseFab: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_expense_list, container, false)
        dbHelper = VehicleDBHelper(activity!!.applicationContext, VehicleContract.tables)
        addExpenseFab = view.findViewById(R.id.add_expense_fab)
        val mainActivity: MainActivity? = activity as MainActivity?
        currentVehicle = mainActivity!!.getCurrentVehicle()

        addExpenseFab!!.setOnClickListener{
            run {
                val intent = Intent(activity, AddExpenseActivity::class.java)
                intent.putExtra("carId", currentVehicle!!.getId())
                startActivity(intent)
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        expenses_list.adapter = ExpenseListAdapter(getExpenses())
        expenses_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        expenses_list.setEmptyView(no_expenses)
        expenses_list.setHasFixedSize(true)
        super.onActivityCreated(savedInstanceState)
    }

    private fun getExpenses() =
        if (currentVehicle != null)
            dbHelper!!.getExpensesForVehicle(currentVehicle!!.getId())
        else listOf()
}