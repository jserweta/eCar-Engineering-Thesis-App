package com.agh.engineeringthesis.eCar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.R
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {
    private var distanceInput: TextView? = null
    private var pricePerLiterInput: TextView? = null
    private var fuelAmountInput: TextView? = null
    private var fuelConsumptionResult: TextView? = null

    private var fuelConsumptionFieldInput: TextView? = null
    private var passengerAmountInput: TextView? = null
    private var totalCostResultInput: TextView? = null
    private var perUserCostResult: TextView? = null

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        distanceInput = findViewById(R.id.distance_field)
        pricePerLiterInput = findViewById(R.id.price_per_liter_field)
        fuelAmountInput = findViewById(R.id.fuel_liter_field)
        fuelConsumptionResult = findViewById(R.id.cons_result_field)

        fuelConsumptionFieldInput = findViewById(R.id.fuel_consumption_field)
        passengerAmountInput = findViewById(R.id.passenger_amount_field)
        totalCostResultInput = findViewById(R.id.cost_result_field)
        perUserCostResult = findViewById(R.id.cost_peruser_result_field)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        consumption_calc_btn.setOnClickListener {
            run {
                if (distanceInput!!.text.toString().toDoubleOrNull() == null ||
                    fuelAmountInput!!.text.toString().toDoubleOrNull() == null){
                Toast.makeText(this, "Wypełnij pola: Odległość, Ilość paliwa", Toast.LENGTH_LONG).show()
                return@run
                }

                fuelConsumptionResult!!.text = String.format("%.2f l/100km",calculateFuelConsumption())
                fuelConsumptionFieldInput!!.text = String.format("%.2f",calculateFuelConsumption()).replace(",",".")
            }
        }

        cost_calc_btn.setOnClickListener {
            run {
                var passengerAmount = 1
                if (distanceInput!!.text.toString().toDoubleOrNull() == null ||
                    fuelConsumptionFieldInput!!.text.toString().toDoubleOrNull() == null ||
                    pricePerLiterInput!!.text.toString().toDoubleOrNull() == null){
                    Toast.makeText(this, "Wypełnij pola:\nOdległość, Cena za litr, Zużycie paliwa\nLiczba pasażerów domyślnie to 1", Toast.LENGTH_LONG).show()
                    return@run
                }

                if (passengerAmountInput!!.text.toString().toIntOrNull() != null) {
                    passengerAmount = passengerAmountInput!!.text.toString().toInt()
                }
                totalCostResultInput!!.text = String.format("%.2f zł",calculateTripCost())
                perUserCostResult!!.text = String.format("%.2f zł",calculateTripCost()/passengerAmount)
            }

        }

//        btnBackCostCalc.setOnClickListener {
//            finish()
//        }
    }

    private fun calculateFuelConsumption(): Double{
        val distance = distanceInput!!.text.toString().toInt()
        val fuelAmount = fuelAmountInput!!.text.toString().toDouble()

        return (fuelAmount / (distance / 100.0))
    }

    private fun calculateTripCost(): Double{
        val pricePerLiter = pricePerLiterInput!!.text.toString().toDouble()
        val distance = distanceInput!!.text.toString().toInt()
        val fuelConsumption = fuelConsumptionFieldInput!!.text.toString().toDouble()

        return ((distance / 100.0) * fuelConsumption * pricePerLiter)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}