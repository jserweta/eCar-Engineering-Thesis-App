package com.agh.engineeringthesis.eCar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agh.engineeringthesis.eCar.R
import com.agh.engineeringthesis.eCar.model.Notification
import kotlinx.android.synthetic.main.notification_list_item.view.*
import java.text.SimpleDateFormat

class NotificationListAdapter(var notificationsList: List<Notification>): RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class NotificationViewHolder(itemView: View, listener: OnItemClickListener?): RecyclerView.ViewHolder(itemView){
        val notificationTitle: TextView = itemView.notification_name_field
        val notificationDate: TextView = itemView.notification_date_field
        val notificationType: TextView = itemView.notification_type_field
        private val deleteBtn: ImageView = itemView.del_notification

        init {
            deleteBtn.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position)
                    }
                }
            }
        }
    }

    fun getElementOnPosition(position: Int): Notification = notificationsList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentItem = notificationsList[position]
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

        holder.notificationTitle.text = String.format("%s", currentItem.notificationName)
        holder.notificationDate.text = formatter.format(currentItem.date)
        holder.notificationType.text = String.format("%s",currentItem.notificationType)
    }

    override fun getItemCount() = notificationsList.size


}
