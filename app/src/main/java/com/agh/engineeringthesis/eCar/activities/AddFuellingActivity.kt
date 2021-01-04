package com.agh.engineeringthesis.eCar.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Fuelling
import com.agh.engineeringthesis.eCar.model.Vehicle
import kotlinx.android.synthetic.main.activity_add_fuelling.*
import java.text.SimpleDateFormat
import java.util.*
import com.agh.engineeringthesis.eCar.db.VehicleContract.FuellingEntry as Fuellings
import com.agh.engineeringthesis.eCar.db.VehicleContract.VehicleEntry as Vehicles

class AddFuellingActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    private var fuellingAmountInput: TextView? = null
    private var fuellingPriceInput: TextView? = null
    private var fuelTypeInput: Spinner? = null
    private var fuellingMileageInput: TextView? = null
    private var addFuellingBtn: Button? = null
    private var fuellingDateInput: TextView? = null
    //private var returnBtn: Button? = null
    private var dbHelper: VehicleDBHelper? = null

    private var toolbar: Toolbar? = null

    private var year = 0
    private var month = 0
    private var day = 0

    private var savedYear = 0
    private var savedMonth = 0
    private var savedDay = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fuelling)

        fuellingAmountInput = findViewById(R.id.fuel_amount)
        fuellingPriceInput = findViewById(R.id.fuel_price)
        fuellingMileageInput = findViewById(R.id.fuelling_mileage)
        fuelTypeInput = findViewById(R.id.fuel_type)
        fuellingDateInput = findViewById(R.id.date_input)
        addFuellingBtn = findViewById(R.id.add_fuelling_btn)
        //returnBtn = findViewById(R.id.btnBackFuelling)
        dbHelper = VehicleDBHelper(this, VehicleContract.tables)
        toolbar = findViewById(R.id.toolbar)

        val vehicleId = intent.getIntExtra("carId", 0)
        val vehicle = dbHelper!!.getById(Vehicles, vehicleId)

        recent_mileage.text = String.format("Ostatni przebieg: %dkm", vehicle!!.mileage)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        fuelTypeInput!!.adapter = ArrayAdapter(this, android.R.layout.simple_selectable_list_item, Fuelling.FuelType.values())

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
        val currentDate: String = simpleDateFormat.format(Date())
        fuellingDateInput!!.text = currentDate

        pickDate()

        addFuellingBtn!!.setOnClickListener {
            run {
                if (fuellingAmountInput!!.text.toString().toDoubleOrNull() == null ||
                        fuellingPriceInput!!.text.toString().toDoubleOrNull() == null ||
                        fuellingMileageInput!!.text.toString().toIntOrNull() == null){
                    Toast.makeText(this, "Uzupełnij poprawnie wszystkie pola!", Toast.LENGTH_LONG).show()
                    return@run
                }

                if (fuellingMileageInput!!.text.toString().toInt() <= vehicle.mileage){
                    Toast.makeText(this, "Nowy przebieg powinnien być większy ostatnio podany", Toast.LENGTH_LONG).show()
                    return@run
                }

                addRefuelling(vehicle)
                Toast.makeText(this, "Dodano tankowanie!", Toast.LENGTH_LONG).show()
                dbHelper!!.update(Vehicles, vehicle.copy(mileage = fuellingMileageInput!!.text.toString().toInt()))
                val intent = Intent("com.agh.engineeringthesis.vehiclemanager.UPDATE_SPINNER")
                sendBroadcast(intent)
                finish()
            }
        }

    }

    private fun addRefuelling(vehicle: Vehicle) {
        val fuellingAmount: Double = fuellingAmountInput!!.text.toString().toDouble()
        val fuellingPrice: Double = fuellingPriceInput!!.text.toString().toDouble()
        val fuellingMileage: Int = fuellingMileageInput!!.text.toString().toInt()
        val fuelType: String = fuelTypeInput!!.selectedItem.toString()
        val fuellingDate: Date = java.sql.Date.valueOf(fuellingDateInput!!.text.toString())

        val fuelling = Fuelling(
            vehicleId = vehicle.getId(),
            fuelAmount = fuellingAmount,
            pricePerLitre = fuellingPrice,
            fuelType = Fuelling.FuelType.valueOf(fuelType),
            lastFuellingMileage = vehicle.mileage,
            mileage = fuellingMileage,
            date = fuellingDate
        )

        dbHelper!!.insert(Fuellings, fuelling)
    }

    private fun getDateTimeCalendar(){
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
    }

    private fun pickDate(){
        getDateTimeCalendar()
        fuellingDateInput!!.setOnClickListener {
            DatePickerDialog(this, this, year,month,day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        fuellingDateInput!!.text = "$savedYear-${savedMonth + 1}-$savedDay"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
