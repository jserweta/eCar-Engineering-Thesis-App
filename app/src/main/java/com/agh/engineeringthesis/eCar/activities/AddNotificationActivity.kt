package com.agh.engineeringthesis.eCar.activities

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.adapters.NotificationListAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Notification
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.agh.engineeringthesis.eCar.notifications.NotificationReceiver
import java.util.*
import com.agh.engineeringthesis.eCar.db.VehicleContract.NotificationEntry as Notifications


class AddNotificationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var toolbar: Toolbar? = null

    private var notificationName: TextView? = null
    private var notificationType: Spinner? = null
    private var notificationDate: TextView? = null
    private var addNotificationBtn: Button? = null

    private var notificationListAdapter: NotificationListAdapter? = null

    private var dbHelper: VehicleDBHelper? = null

    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0

    private var savedYear = 0
    private var savedMonth = 0
    private var savedDay = 0
    private var savedHour = 0
    private var savedMinute = 0

    private var savedDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        createNotificationChannel()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        notificationName = findViewById(R.id.notification_name)
        notificationType = findViewById(R.id.notification_type)
        notificationDate = findViewById(R.id.notification_date_input)

        addNotificationBtn = findViewById(R.id.add_notification_btn)

        dbHelper = VehicleDBHelper(this, VehicleContract.tables)

        //val currentVehicle = NotificationsListActivity().currentVehicle

        val vehicleId = intent.getIntExtra("carId", 0)
        val currentVehicle = dbHelper!!.getById(VehicleContract.VehicleEntry, vehicleId)

        notificationType!!.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_selectable_list_item,
            Notification.NotificationType.values()
        )

//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMANY)
//        currentDate = simpleDateFormat.format(Date())
//        notificationDate!!.text = currentDate

        pickDate()

        addNotificationBtn!!.setOnClickListener {
            run {
                if (notificationName!!.text.toString().isEmpty() ||
                    notificationDate!!.text.toString().isEmpty() ){
                    Toast.makeText(this, "Uzupełnij poprawnie wszystkie pola!", Toast.LENGTH_LONG).show()
                    return@run
                }

                addNotification(currentVehicle!!)
                Toast.makeText(this, "Dodano nowe powiadomienie!", Toast.LENGTH_LONG).show()

                val notifyTime = savedDate!!.timeInMillis

                val intent = Intent(applicationContext, NotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                val alarmManager: AlarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent)
                finish()
            }
        }

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val name = "VehicleManagerChannel"
            val description = "Channel for VehicleManager"
            val channel = NotificationChannel("notifyVehicleManager", name, importance)
            channel.description = description
            channel.setShowBadge(false)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun addNotification(vehicle: Vehicle) {
        val notificationName = notificationName!!.text.toString()

        val notificationDate = savedDate!!.timeInMillis
            //java.sql.Date.valueOf("2020-12-02")
            //
        val notificationType = notificationType!!.selectedItem.toString()


        val notification = Notification(
            vehicleId = vehicle.getId(),
            date = notificationDate,
            notificationName = notificationName,
            notificationType = Notification.NotificationType.valueOf(notificationType)
        )

        dbHelper!!.insert(Notifications, notification)
    }

    private fun getDateTimeCalendar(){
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        hour = c.get(Calendar.HOUR_OF_DAY)
        minute = c.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        getDateTimeCalendar()
        notificationDate!!.setOnClickListener {
            val datePicker = DatePickerDialog(this, this, year, month, day)
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()



    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedMinute = minute
        savedHour = hourOfDay
        savedDate = Calendar.getInstance()
        savedDate!!.set(savedYear, savedMonth, savedDay, savedHour, savedMinute, 0)
        val currentDateTime: Calendar = Calendar.getInstance()
        if (savedDate!!.timeInMillis - currentDateTime.timeInMillis > 0) {
            notificationDate!!.text = "$savedYear-${savedMonth + 1}-$savedDay $savedHour:$savedMinute"
        }else{
            Toast.makeText(this, "Wybierz popraną datę!", Toast.LENGTH_LONG).show()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}