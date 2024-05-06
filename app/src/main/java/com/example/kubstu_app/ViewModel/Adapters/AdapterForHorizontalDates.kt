package com.example.kubstu_app.ViewModel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kubstu_app.ViewModel.HorizontalDateItemClickListener
import com.example.kubstu_app.Model.HorizontalDateModel
import com.example.kubstu_app.R
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Date

class AdapterForHorizontalDates(private val dates: List<HorizontalDateModel>,
                                private val context: Context,
                                private val WeekDates: List<String>,
                                private val FormatDate: ArrayList<Date>,
                                private val Listener: HorizontalDateItemClickListener
) : Adapter<ViewHolder>() {
    var pos = -1


    inner class HorizontalDateViewHolder(itemView: View) : ViewHolder(itemView) {
        internal val weekday: TextView = itemView.findViewById(R.id.WeekDayTV)
        internal val date: TextView = itemView.findViewById(R.id.DateTV)
        internal val dategrnd: ImageView = itemView.findViewById(R.id.HorizontalCalendar_selectedBckGrnd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horisontalcalendar_item, parent, false)
        return HorizontalDateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emonth = DateFormatSymbols().months
        when (holder) {
            is HorizontalDateViewHolder -> {

                holder.weekday.text = dates[position].day
                holder.date.text = dates[position].date.toString()
                holder.dategrnd.setOnClickListener{
                    val date = WeekDates[position]
                    val dList = date.split("-")
                    Listener.onDateClick("${dList[1]}-${dList[0]}-${dList[2]}", position)
                    val calendar = Calendar.getInstance()
                    calendar.time = FormatDate[position]
                    month = emonth[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR)
                    pos = position
                    notifyDataSetChanged()
                }
                dates[position].isSelected = pos == position
                if(dates[position].isSelected) {
                    holder.date.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    holder.weekday.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    holder.dategrnd.setColorFilter(ContextCompat.getColor(context, R.color.white)) //--------------------------------------
                }
                else {
                    holder.date.setTextColor(ContextCompat.getColor(context, R.color.white))
                    holder.weekday.setTextColor(ContextCompat.getColor(context, R.color.white))
                    holder.dategrnd.setColorFilter(ContextCompat.getColor(context, R.color.blue)) //--------------------------------------
                }
            }
            else -> throw IllegalArgumentException("Invalid holder type")
        }
    }

    fun UpdatePosition(pos: Int) {
        this.pos = pos
        notifyDataSetChanged()
    }

    companion object {
        var month: String? = null
    }

}