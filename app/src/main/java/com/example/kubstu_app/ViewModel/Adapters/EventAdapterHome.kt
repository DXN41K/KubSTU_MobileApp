package com.example.kubstu_app.ViewModel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.R

class EventAdapterHome(private val count: Int) : RecyclerView.Adapter<EventAdapterHome.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.GetEventBtn)

        init {
            button.setOnClickListener {
                Toast.makeText(itemView.context, "Подробнее", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.eventrecyclingview, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Конфигурация содержимого для каждого элемента, если это необходимо
    }

    override fun getItemCount(): Int {
        return count
    }
}