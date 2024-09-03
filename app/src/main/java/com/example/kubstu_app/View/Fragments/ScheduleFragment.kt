package com.example.kubstu_app.View.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.ViewModel.Retrofit.IAPI
import com.example.kubstu_app.Model.timeTable.Products
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.ScheduleLessonsAdapter
import com.example.kubstu_app.ViewModel.Retrofit.retrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.Calendar


class ScheduleFragment : Fragment() {

    private lateinit var view: View
    lateinit var lessadapter: ScheduleLessonsAdapter

    private var selectedDate: Calendar = Calendar.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    var currYear: Int = 0
    var DuplcurrYear: Int = 0
    var currMonth: Int = 0
    var currDay: Int = 0
    var _sem: Int = 0

    val monthToString = mapOf(
        0 to "Января",
        1 to "Февраля",
        2 to "Марта",
        3 to "Апреля",
        4 to "Мая",
        5 to "Июня",
        6 to "Июля",
        7 to "Августа",
        8 to "Сентября",
        9 to "Октбря",
        10 to "Ноября",
        11 to "Декабря"
    )

    private lateinit var selectedGroup: String

    var list: Products? = null
    lateinit var retro: Retrofit
    lateinit var client: OkHttpClient
    lateinit var lessonAPI: IAPI


    override fun onAttach(context: Context) {
        super.onAttach(context)

        retrofitClient.Initialize(context)
        retro = retrofitClient.getClient()
        lessonAPI = retro.create(IAPI::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_schedule, container, false)

        sharedPreferences = requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        selectedGroup = sharedPreferences.getString("Users_group", null)!!

        //настраиваем обработчик выбора даты
        val monthText: TextView = view.findViewById(R.id.month)

        val currentDate = Calendar.getInstance()
        currDay = currentDate.get(Calendar.DAY_OF_MONTH)
        currMonth = currentDate.get(Calendar.MONTH)
        currYear = currentDate.get(Calendar.YEAR)
        DuplcurrYear = currYear
        _sem = 1
        if (currMonth < 8)
        {
            DuplcurrYear = currYear - 1
            _sem = 2
        }

        monthText.text = "$currDay ${monthToString[currMonth]}"


        //настраиваем recycling view

        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsSchefuleRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lessadapter = ScheduleLessonsAdapter(requireContext())
        LessRecyclerView.adapter = lessadapter



        CoroutineScope(Dispatchers.IO).launch {
            try {
                list = lessonAPI.getThen(selectedGroup, DuplcurrYear, _sem, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
            }
            catch (e: Exception) {
            }
            if (isAdded){
                requireActivity().runOnUiThread {
                    lessadapter.submitList(list?.data?.sortedBy { it.pair })
                    if (lessadapter.itemCount == 0) view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 1f
                    else view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 0f
                }
            }
        }


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedGroup = parent.getItemAtPosition(position) as String
                    //реализация при смене
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            list = lessonAPI.getThen(selectedGroup, DuplcurrYear, _sem, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
                        }
                        catch (e: Exception) {
                        }
                        if (isAdded){
                            requireActivity().runOnUiThread {
                                lessadapter.submitList(list?.data?.sortedBy { it.pair })
                                if (lessadapter.itemCount == 0) view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 1f
                                else view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 0f
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        val monthText: TextView = view.findViewById(R.id.month)

        monthText.setOnClickListener{
            showCal()
        }
    }
    private fun showCal() {
        //получение сущности календаря
        val cal = selectedDate


        val dateListener = DatePickerDialog.OnDateSetListener{ _, year, month, dayofmonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayofmonth)

            val monthText = view.findViewById<TextView>(R.id.month)

            this._sem = 1
            this.currYear = year

            this.DuplcurrYear = this.currYear
            this.currMonth = month
            this.currDay = dayofmonth
            monthText.text = "$dayofmonth ${monthToString[month]}"
            if (currMonth < 8) {
                this.DuplcurrYear = currYear - 1
                _sem = 2
            }

            //обработка выбранной даты будет здесь
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    list = lessonAPI.getThen(selectedGroup, DuplcurrYear, _sem, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
                }
                catch (e: Exception) {
                }
                if (isAdded){
                    requireActivity().runOnUiThread {
                        lessadapter.submitList(list?.data?.sortedBy { it.pair })
                        if (lessadapter.itemCount == 0) view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 1f
                        else view.findViewById<TextView>(R.id.ScheduleNoPairText).alpha = 0f
                    }
                }
            }
        }

        val datePicker = DatePickerDialog(requireContext(),
            dateListener, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))


        datePicker.show()
    }

}