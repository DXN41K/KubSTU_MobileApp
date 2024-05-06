package com.example.kubstu_app.View.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.HorizontalCalendar
import com.example.kubstu_app.ViewModel.HorizontalDateItemClickListener
import com.example.kubstu_app.databinding.ActivityMainBinding
import kotlinx.coroutines.yield
import java.util.Calendar

class ScheduleFragment : Fragment(), HorizontalDateItemClickListener {

    private var horizontalCalendar: HorizontalCalendar? = null
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val month: TextView = view.findViewById(R.id.month)
        val datesRv: RecyclerView = view.findViewById(R.id.dates_rv)

        month.setOnClickListener{
            showCal()
        }

        horizontalCalendar = HorizontalCalendar(this, datesRv, month, requireContext())

        return view
    }

    override fun onDateClick(date: String, position: Int) {

    }

    private fun showCal() {
        val cal = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayofmonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayofmonth)
            horizontalCalendar!!.setCalendarDate(year, month, dayofmonth, this, view.findViewById(R.id.month))
        }
        val datePicker = DatePickerDialog(requireContext(), dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
        view.findViewById<TextView>(R.id.month).setOnClickListener{
            val datePicker = DatePickerDialog(requireContext(), dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }
    }
}