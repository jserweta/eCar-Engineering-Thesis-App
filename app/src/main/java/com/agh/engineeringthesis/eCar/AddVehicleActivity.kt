package com.agh.engineeringthesis.eCar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Vehicle
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import kotlinx.android.synthetic.main.activity_main.*

class AddVehicleActivity : AppCompatActivity() {

    private var addVehicleBtn: Button? = null
    private var vehicleNameInput: EditText? = null
    private var vehicleTypeInput: Spinner? = null
    private var vehicleMileageInput: EditText? = null
    private var dbHelper: VehicleDBHelper? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        addVehicleBtn = findViewById(R.id.add_vehicle_btn)
        vehicleNameInput = findViewById(R.id.vehicle_name_field)
        vehicleTypeInput = findViewById(R.id.vehicle_type_input)
        vehicleMileageInput = findViewById(R.id.mileage_input_field)

        vehicleTypeInput!!.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_selectable_list_item,
            Vehicle.VehicleType.values()
        )
        dbHelper = VehicleDBHelper(this, VehicleContract.tables)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (intent.getBooleanExtra("emptyVehicleList", false)){
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
        }

        addVehicleBtn!!.setOnClickListener {
            run {
                if(vehicleNameInput!!.text.toString().isEmpty() ||
                    vehicleMileageInput!!.text.toString().toIntOrNull() == null) {
                    Toast.makeText(this, "Uzupełnij poprawnie wszystkie pola!", Toast.LENGTH_LONG).show()
                    return@run
                }else if (vehicleNameInput!!.text.toString().length >20){
                    Toast.makeText(this, "Za długa nazwa! \n Maksymalnie 20 znaków.", Toast.LENGTH_LONG).show()
                    return@run
                }

                val vehicleName = vehicleNameInput!!.text.toString()
                val vehicleType = vehicleTypeInput!!.selectedItem.toString()
                val mileage = vehicleMileageInput!!.text.toString().toInt()

                val newVehicle = Vehicle(
                    0,
                    vehicleName,
                    Vehicle.VehicleType.valueOf(vehicleType),
                    mileage
                )
                dbHelper!!.insert(VehicleContract.VehicleEntry, newVehicle)
                Toast.makeText(this, "Dodano nowy pojazd!", Toast.LENGTH_LONG).show()
                vehicleNameInput!!.text.clear()
                vehicleMileageInput!!.text.clear()

                val intent = Intent("com.agh.engineeringthesis.vehiclemanager.UPDATE_SPINNER")
                sendBroadcast(intent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        if (!intent.getBooleanExtra("emptyVehicleList", false)){
            super.onBackPressed()
        }
    }

}