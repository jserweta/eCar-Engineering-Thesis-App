package com.agh.engineeringthesis.eCar

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Expense
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.agh.engineeringthesis.eCar.db.VehicleContract.VehicleEntry as Vehicles
import com.agh.engineeringthesis.eCar.db.VehicleContract.ExpenseEntry as Expenses
import kotlinx.android.synthetic.main.activity_add_expense.*
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var expenseName: TextView? = null
    private var expenseValue: TextView? = null
    private var expenseDate: TextView? = null
    private var expenseType: Spinner? = null
    private var addExpenseBtn: Button? = null

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
        setContentView(R.layout.activity_add_expense)

        expenseName = findViewById(R.id.expense_name)
        expenseValue = findViewById(R.id.expense_value)
        expenseDate = findViewById(R.id.expense_date_input)
        expenseType = findViewById(R.id.expense_type)
        addExpenseBtn = findViewById(R.id.add_expense_btn)

        dbHelper = VehicleDBHelper(this, VehicleContract.tables)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val vehicleId = intent.getIntExtra("carId", 0)
        val vehicle = dbHelper!!.getById(Vehicles, vehicleId)

        expenseType!!.adapter = ArrayAdapter(this, android.R.layout.simple_selectable_list_item, Expense.ExpenseType.values())

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
        val currentDate: String = simpleDateFormat.format(Date())
        expenseDate!!.text = currentDate

        pickDate()

        addExpenseBtn!!.setOnClickListener {
            run {
                if (expenseName!!.text.toString().isEmpty() ||
                        expenseValue!!.text.toString().toDoubleOrNull() == null){
                    Toast.makeText(this, "Uzupełnij poprawnie wszystkie pola!", Toast.LENGTH_LONG).show()
                    return@run
                }

                addExpense(vehicle)
                finish()
            }
        }

    }

    private fun addExpense(vehicle: Vehicle?) {
        val expenseName: String = expenseName!!.text.toString()
        val expenseValue: Double = expenseValue!!.text.toString().toDouble()
        val expenseType: String = expenseType!!.selectedItem.toString()
        val expenseDate: Date = java.sql.Date.valueOf(expenseDate!!.text.toString())

        val expense = Expense(
            vehicleId = vehicle!!.getId(),
            date = expenseDate,
            costName = expenseName,
            costValue = expenseValue,
            expenseType = Expense.ExpenseType.valueOf(expenseType)
        )

        dbHelper!!.insert(Expenses, expense)
        Toast.makeText(this, "Dodano wydatek!", Toast.LENGTH_LONG).show()
    }

    private fun getDateTimeCalendar(){
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
    }

    private fun pickDate(){
        getDateTimeCalendar()
        expenseDate!!.setOnClickListener {
            DatePickerDialog(this, this, year,month,day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        expenseDate!!.text = "$savedYear-${savedMonth + 1 }-$savedDay"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}