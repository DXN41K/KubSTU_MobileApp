package com.example.kubstu_app.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.response.LessonResponse
import com.example.kubstu_app.presentation.viewstate.SvcCardViewState
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.AdAdapter
import com.example.kubstu_app.ViewModel.Adapters.EventAdapterHome
import com.example.kubstu_app.ViewModel.Adapters.HomeSpecialAdapter
import com.example.kubstu_app.ViewModel.Adapters.ScheduleLessonsAdapter
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar


class HomeFragment : Fragment() {

    lateinit var lessadapter: ScheduleLessonsAdapter

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var retro: Retrofit
    lateinit var lessonAPI: IAPI

    private lateinit var adRecyclerView: RecyclerView
    private lateinit var eventRecyclerView: RecyclerView

    val monthToString = mapOf(
        0 to "января",
        1 to "февраля",
        2 to "марта",
        3 to "апреля",
        4 to "мая",
        5 to "июня",
        6 to "июля",
        7 to "августа",
        8 to "сентября",
        9 to "октября",
        10 to "ноября",
        11 to "декабря"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        val userNameTV = view.findViewById<TextView>(R.id.UserNametTV)
        val name: String? = sharedPreferences.getString("imja", null)
        userNameTV.text = if (name == null) "Пользователь!" else "${name}!"

//        val settingsServices = view.findViewById<CardView>(R.id.servicebtn)
//        settingsServices.setOnClickListener {
//            val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//            navbar.selectedItemId = R.id.profile
//        }

        val scheduleBtn = view.findViewById<Button>(R.id.GoToScheduleBtn)
        scheduleBtn.setOnClickListener {
            val navbar =
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            navbar.selectedItemId = R.id.schedule
        }


        view.findViewById<TextView>(R.id.GoodDayText).text =
            when (getCurrentTime()) {
                in 4..11 -> "Доброе утро, "
                in 12..17 -> "Добрый день, "
                else -> "Добрый вечер, "
            }


        adRecyclerView = view.findViewById(R.id.AdRecyclingView)
        adRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adRecyclerView.adapter = AdAdapter(2)


        eventRecyclerView = view.findViewById(R.id.EventsRecyclingView)
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        eventRecyclerView.adapter = EventAdapterHome(2)  // Установка адаптера для отображения двух элементов



        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lessadapter = ScheduleLessonsAdapter(requireContext())
        LessRecyclerView.adapter = lessadapter


        RetrofitClient.Initialize(requireContext())
        retro = RetrofitClient.getClient()
        lessonAPI = retro.create(IAPI::class.java)


        var year = getCurrentYear()
        val month = getCurrentMonth()
        val day = getCurrentDay()
        val textWeek = view.findViewById<TextView>(R.id.profileWeekText)

        textWeek.text =
            "$day ${monthToString[month]}, " + when (getCurrentStudyWeek(day, month, year) % 2) {
                0 -> "четная неделя"
                1 -> "нечетная неделя"
                else -> "неделя не распознана"
            }

        var sem = 1

        if (month < 8) {
            year--
            sem = 2
        }

        val group = sharedPreferences.getString("gr", null)

        var list: LessonResponse? = null
        var isZero = false
        lifecycleScope.launch(Dispatchers.IO) {
            try {

                list = lessonAPI.getNow(group ?: "", year, sem)
                isZero = list?.data?.size == 0
            } catch (e: Exception) {
            }
            if (isAdded) {
                requireActivity().runOnUiThread {

                    val lessonsList = list?.data?.sortedBy { it.pair }
                        ?: emptyList() // Если нет данных, возвращаем пустой список
                    lessadapter.submitList(lessonsList)
                    if (isZero) {
                        view.findViewById<View>(R.id.noScheduleHomeItem).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.LessonsRecyclingView).visibility = View.GONE
                    } else {
                        view.findViewById<View>(R.id.LessonsRecyclingView).visibility =
                            View.VISIBLE
                        view.findViewById<View>(R.id.noScheduleHomeItem).visibility = View.GONE
                    }
                }
            }
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val SpecialRV = view.findViewById<RecyclerView>(R.id.SpecialRecyclingView)
        val specialItems = InitSpecialRecyclingView() // Получаем список элементов
        val adapter = HomeSpecialAdapter(specialItems, requireContext())

        // Настройте LayoutManager
        SpecialRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Установите адаптер для RecyclerView
        SpecialRV.adapter = adapter
    }


    private fun InitSpecialRecyclingView(): List<SvcCardViewState> {
        val specialItems = mutableListOf<SvcCardViewState>()
        specialItems.add(
            SvcCardViewState(
                "Онлайн-печать", "низкие цены",
                R.drawable.backgroundgradient_light_to_darkblue, R.drawable.paper_plane_pic,
            "https://print.kubstu.ru")
        )
        //specialItems.add(SpecialDomain("Пропуск", "", R.drawable.backgroundgradient_darkgray))
        return specialItems
    }

    fun getCurrentTime(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH)
    }

    fun getCurrentStudyWeek(currDay: Int, currMonth: Int, currYear: Int): Int {
        // Приведение месяца из Calendar (от 0 до 11) к формату LocalDate (от 1 до 12)
        val currentDate = LocalDate.of(currYear, currMonth + 1, currDay)

        // Определяем 1 сентября текущего года
        val firstSeptember = LocalDate.of(currYear, 9, 1)

        // Если текущая дата до 1 сентября, используем 1 сентября прошлого года
        val startDate = if (currentDate.isBefore(firstSeptember)) {
            LocalDate.of(currYear - 1, 9, 1)
        } else {
            firstSeptember
        }

        // Разница в неделях
        val weeks = ChronoUnit.WEEKS.between(startDate, currentDate)

        return weeks.toInt()
    }

}