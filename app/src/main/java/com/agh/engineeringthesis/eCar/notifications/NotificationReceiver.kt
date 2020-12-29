package com.agh.engineeringthesis.eCar.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.model.Notification

class NotificationReceiver: BroadcastReceiver(){

    private var notificationList: List<Notification>? = null
    private var dbHelper: VehicleDBHelper? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        dbHelper = VehicleDBHelper(context!!, VehicleContract.tables)
        notificationList = dbHelper!!.getAll(VehicleContract.NotificationEntry)
        val lastNotification = notificationList!!.last()
        val message = lastNotification.notificationName
        val title = lastNotification.notificationType.toString()
        val builder = NotificationCompat.Builder(context!!, "notifyVehicleManager").apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(false)
        }


        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(lastNotification.getId(), builder.build())

    }
}