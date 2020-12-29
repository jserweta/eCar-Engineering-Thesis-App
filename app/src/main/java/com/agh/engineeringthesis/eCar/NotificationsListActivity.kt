package com.agh.engineeringthesis.eCar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.agh.engineeringthesis.eCar.adapters.NotificationListAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Notification
import com.agh.engineeringthesis.eCar.model.Vehicle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_notifications_list.*
import com.agh.engineeringthesis.eCar.db.VehicleContract.NotificationEntry as Notifications

class NotificationsListActivity: AppCompatActivity() {

    private var addNotificationFab: FloatingActionButton? = null
    private var dbHelper: VehicleDBHelper? = null
    private var notificationListAdapter: NotificationListAdapter? = null
    var currentVehicle: Vehicle? = null

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_list)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        addNotificationFab = findViewById(R.id.add_notification_fab)
        dbHelper = VehicleDBHelper(this, VehicleContract.tables)

        val vehicleId = intent.getIntExtra("vehicleID", 0)
        currentVehicle = dbHelper!!.getById(VehicleContract.VehicleEntry, vehicleId)

        addNotificationFab!!.setOnClickListener {
            val intent = Intent(this, AddNotificationActivity::class.java)
            intent.putExtra("carId", vehicleId)
            startActivity(intent)
        }



        notificationListAdapter = NotificationListAdapter(getNotifications())
        notificationListAdapter!!.setOnItemClickListener(object : NotificationListAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                val notificationToRemove: Notification = notificationListAdapter!!.getElementOnPosition(position)

                dbHelper!!.deleteById(Notifications, notificationToRemove.getId())
                notificationListAdapter!!.notificationsList = getNotifications()
                notificationListAdapter!!.notifyItemRemoved(position)
            }
        })

        notifications_list.adapter = notificationListAdapter
        notifications_list.layoutManager = LinearLayoutManager(this)
        notifications_list.setEmptyView(no_notifications)
        notifications_list.hasFixedSize()



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        updateNotificationsList()
    }
    private fun updateNotificationsList() {
        notificationListAdapter!!.notificationsList = getNotifications()
        notificationListAdapter!!.notifyDataSetChanged()
    }

    private fun getNotifications() =
        if (currentVehicle != null)
            dbHelper!!.getNotificationsForVehicle(currentVehicle!!.getId())
        else listOf()
}