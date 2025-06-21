package com.example.kubstu_app.presentation.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.response.LessonResponse
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.ScheduleLessonsAdapter
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import com.example.kubstu_app.presentation.activity.ChooseGroupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar


class ScheduleFragment : Fragment() {

    private lateinit var view: View
    lateinit var lessadapter: ScheduleLessonsAdapter
    lateinit var tomorrowBtn: Button

    private var selectedDate: Calendar = Calendar.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    var currYear: Int = 0
    var DuplcurrYear: Int = 0
    var currMonth: Int = 0
    var currDay: Int = 0
    var _sem: Int = 0
    var currDayOfWeek: Int = 1
    var isToday: Boolean = true

    val dayOfWeekToString = mapOf(
        1 to "Воскресенье",
        2 to "Понедельник",
        3 to "Вторник",
        4 to "Среда",
        5 to "Четверг",
        6 to "Пятница",
        7 to "Суббота",
    )

    private val monthToString = mapOf(
        0 to "Января",
        1 to "Февраля",
        2 to "Марта",
        3 to "Апреля",
        4 to "Мая",
        5 to "Июня",
        6 to "Июля",
        7 to "Августа",
        8 to "Сентября",
        9 to "Октября",
        10 to "Ноября",
        11 to "Декабря",
    )

    private lateinit var selectedGroup: String
    private lateinit var chooseGroupActivityResultLauncher: ActivityResultLauncher<Intent>

    var list: LessonResponse? = null
    lateinit var retro: Retrofit
    lateinit var lessonAPI: IAPI


    override fun onAttach(context: Context) {
        super.onAttach(context)

        RetrofitClient.Initialize(context)
        retro = RetrofitClient.getClient()
        lessonAPI = retro.create(IAPI::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_schedule, container, false)

        chooseGroupActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // Этот блок выполняется, когда ChooseGroupActivity завершается и возвращает результат
            if (result.resultCode == Activity.RESULT_OK) { // Используем Activity.RESULT_OK, так как ActivityResultContracts требует его
                val data: Intent? = result.data
                val selectedGroupName = data?.getStringExtra("result")
                selectedGroupName?.let {
                    // Здесь у вас есть выбранное имя группы!
                    // Вы можете обновить TextView во фрагменте, сохранить его и т.д.
                    // Например, если у вас есть TextView с ID groupNameDisplayTextView:
                    // val groupNameDisplayTextView: TextView = view.findViewById(R.id.groupNameDisplayTextView)
                    // groupNameDisplayTextView.text = "Выбрана группа: $it"
                    println("Выбрана группа: $it")
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Обработка случая, если ChooseGroupActivity была отменена (например, пользователь нажал "Назад")
                println("Выбор группы отменен.")
            }
        }

        // Находим вашу кнопку по ID (предполагается, что она есть в R.layout.fragment_schedule)
        val selectGroupButton: Button = view.findViewById(R.id.Select_Group_Schedule_Btn)

        // Устанавливаем слушатель нажатий для вашей кнопки
        selectGroupButton.setOnClickListener {
            // Создаем Intent для открытия ChooseGroupActivity
            val intent = Intent(requireContext(), ChooseGroupActivity::class.java) // Используем requireContext()
            // Запускаем активити и ожидаем результат
            chooseGroupActivityResultLauncher.launch(intent)
        }



        sharedPreferences =
            requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        selectedGroup = sharedPreferences.getString("gr", null)!!

        //настраиваем обработчик выбора даты
        val monthText: TextView = view.findViewById(R.id.month)

        val currentDate = Calendar.getInstance()
        currDay = currentDate.get(Calendar.DAY_OF_MONTH)
        currMonth = currentDate.get(Calendar.MONTH)
        currYear = currentDate.get(Calendar.YEAR)
        currDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK)
        DuplcurrYear = currYear

        view.findViewById<TextView>(R.id.isOddWeekText).text =
            when (getCurrentStudyWeek(currDay, currMonth, currYear) % 2) {
                0 -> "Четная неделя"
                1 -> "Нечетная неделя"
                else -> "Неделя не распознана"
            }

        _sem = 1
        if (currMonth < 8) {
            DuplcurrYear = currYear - 1
            _sem = 2
        }

        monthText.text = "${dayOfWeekToString[currDayOfWeek]}, $currDay ${monthToString[currMonth]}"


        //настраиваем recycling view

        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsSchefuleRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lessadapter = ScheduleLessonsAdapter(requireContext())
        LessRecyclerView.adapter = lessadapter


        var isZero = false
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                list = lessonAPI.getThen(
                    selectedGroup,
                    DuplcurrYear,
                    _sem,
                    "$currYear-${currMonth + 1}-$currDay",
                    "$currYear-${currMonth + 1}-$currDay"
                )
                isZero = list?.data?.size == 0
            } catch (e: Exception) {
            }
            if (isAdded) {
                requireActivity().runOnUiThread {

                    val lessonsList = list?.data?.sortedBy { it.pair }
                        ?: emptyList() // Если нет данных, возвращаем пустой список
                    lessadapter.submitList(lessonsList)
                    if (isZero) {
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.GONE
                    } else {
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.VISIBLE
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.GONE
                    }
                }
            }
        }

        tomorrowBtn = view.findViewById(R.id.NextDay_Schedule_Btn)

        tomorrowBtn.setOnClickListener {
            if (isToday) {
                getTomorrow()
                tomorrowBtn.setText("Перейти к расписанию на сегодня")
            } else {
                returnToday()
                tomorrowBtn.setText("Перейти к расписанию на завтра")
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
                    var isZero = false
                    //реализация при смене
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            list = lessonAPI.getThen(
                                selectedGroup,
                                DuplcurrYear,
                                _sem,
                                "$currYear-${currMonth + 1}-$currDay",
                                "$currYear-${currMonth + 1}-$currDay"
                            )
                            isZero = list?.data?.size == 0
                        } catch (e: Exception) {
                        }
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                if (isAdded) {
                                    requireActivity().runOnUiThread {

                                        val lessonsList = list?.data?.sortedBy { it.pair }
                                            ?: emptyList() // Если нет данных, возвращаем пустой список
                                        lessadapter.submitList(lessonsList)
                                        if (isZero) {
                                            view.findViewById<View>(R.id.noScheduleItem).visibility =
                                                View.VISIBLE
                                            view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                                                View.GONE
                                        } else {
                                            view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                                                View.VISIBLE
                                            view.findViewById<View>(R.id.noScheduleItem).visibility =
                                                View.GONE
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        val ScheduleBtn: ImageButton = view.findViewById(R.id.Schedule_Btn)

        ScheduleBtn.setOnClickListener {
            showCal()
        }
    }

    private fun showCal() {
        //получение сущности календаря
        val cal = selectedDate


        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayofmonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayofmonth)

            val monthText = view.findViewById<TextView>(R.id.month)

            this._sem = 1
            this.currYear = year

            this.DuplcurrYear = this.currYear
            this.currMonth = month
            this.currDay = dayofmonth
            this.currDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

            checkIfToday(this.currYear, this.currMonth, this.currDay)

            if (isToday) {
                tomorrowBtn.setText("Перейти к расписанию на завтра")
            } else {
                tomorrowBtn.setText("Перейти к расписанию на сегодня")
            }

            view.findViewById<TextView>(R.id.isOddWeekText).text =
                when (getCurrentStudyWeek(currDay, currMonth, currYear) % 2) {
                    0 -> "Четная неделя"
                    1 -> "Нечетная неделя"
                    else -> "Неделя не распознана"
                }

            monthText.text =
                "${dayOfWeekToString[currDayOfWeek]}, $currDay ${monthToString[currMonth]}"

            if (currMonth < 8) {
                this.DuplcurrYear = currYear - 1
                _sem = 2
            }

            var isZero = false
            //обработка выбранной даты будет здесь
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    list = lessonAPI.getThen(
                        selectedGroup,
                        DuplcurrYear,
                        _sem,
                        "$currYear-${currMonth + 1}-$currDay",
                        "$currYear-${currMonth + 1}-$currDay"
                    )
                    isZero = list?.data?.size == 0
                } catch (e: Exception) {
                    Log.e("eeee", "${e.message}")
                }
                if (isAdded) {
                    requireActivity().runOnUiThread {

                        val lessonsList = list?.data?.sortedBy { it.pair }
                            ?: emptyList() // Если нет данных, возвращаем пустой список
                        lessadapter.submitList(lessonsList)
                        if (isZero) {
                            view.findViewById<View>(R.id.noScheduleItem).visibility = View.VISIBLE
                            view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                                View.GONE
                        } else {
                            view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                                View.VISIBLE
                            view.findViewById<View>(R.id.noScheduleItem).visibility = View.GONE
                        }
                    }
                }
            }
        }

        val datePicker = DatePickerDialog(
            requireContext(),
            dateListener, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )


        datePicker.show()
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

    fun returnToday() {
        val today = Calendar.getInstance()
        isToday = true
        selectedDate = today

        this.currYear = today.get(Calendar.YEAR)
        this.currMonth = today.get(Calendar.MONTH)
        this.currDay = today.get(Calendar.DAY_OF_MONTH)
        this.currDayOfWeek = today.get(Calendar.DAY_OF_WEEK)

        this.currYear = today.get(Calendar.YEAR)
        this.currMonth = today.get(Calendar.MONTH)
        this.currDay = today.get(Calendar.DAY_OF_MONTH)
        this.currDayOfWeek = today.get(Calendar.DAY_OF_WEEK)

        val monthText = view.findViewById<TextView>(R.id.month)

        view.findViewById<TextView>(R.id.isOddWeekText).text =
            when (getCurrentStudyWeek(currDay, currMonth, currYear) % 2) {
                0 -> "Четная неделя"
                1 -> "Нечетная неделя"
                else -> "Неделя не распознана"
            }

        monthText.text =
            "${dayOfWeekToString[currDayOfWeek]}, $currDay ${monthToString[currMonth]}"

        if (currMonth < 8) {
            this.DuplcurrYear = currYear - 1
            _sem = 2
        }

        var isZero = false
        //обработка выбранной даты будет здесь
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                list = lessonAPI.getThen(
                    selectedGroup,
                    DuplcurrYear,
                    _sem,
                    "$currYear-${currMonth + 1}-$currDay",
                    "$currYear-${currMonth + 1}-$currDay"
                )
                isZero = list?.data?.size == 0
            } catch (e: Exception) {
                Log.e("eeee", "${e.message}")
            }
            if (isAdded) {
                requireActivity().runOnUiThread {

                    val lessonsList = list?.data?.sortedBy { it.pair }
                        ?: emptyList() // Если нет данных, возвращаем пустой список
                    lessadapter.submitList(lessonsList)
                    if (isZero) {
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.GONE
                    } else {
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.VISIBLE
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.GONE
                    }
                }
            }
        }
    }

    fun getTomorrow() {
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_MONTH, 1)
        isToday = false
        selectedDate = today

        this.currYear = today.get(Calendar.YEAR)
        this.currMonth = today.get(Calendar.MONTH)
        this.currDay = today.get(Calendar.DAY_OF_MONTH)
        this.currDayOfWeek = today.get(Calendar.DAY_OF_WEEK)

        val monthText = view.findViewById<TextView>(R.id.month)

        view.findViewById<TextView>(R.id.isOddWeekText).text =
            when (getCurrentStudyWeek(currDay, currMonth, currYear) % 2) {
                0 -> "Четная неделя"
                1 -> "Нечетная неделя"
                else -> "Неделя не распознана"
            }

        monthText.text =
            "${dayOfWeekToString[currDayOfWeek]}, $currDay ${monthToString[currMonth]}"

        if (currMonth < 8) {
            this.DuplcurrYear = currYear - 1
            _sem = 2
        }

        var isZero = false
        //обработка выбранной даты будет здесь
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                list = lessonAPI.getThen(
                    selectedGroup,
                    DuplcurrYear,
                    _sem,
                    "$currYear-${currMonth + 1}-$currDay",
                    "$currYear-${currMonth + 1}-$currDay"
                )
                isZero = list?.data?.size == 0
            } catch (e: Exception) {
                Log.e("eeee", "${e.message}")
            }
            if (isAdded) {
                requireActivity().runOnUiThread {

                    val lessonsList = list?.data?.sortedBy { it.pair }
                        ?: emptyList() // Если нет данных, возвращаем пустой список
                    lessadapter.submitList(lessonsList)
                    if (isZero) {
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.GONE
                    } else {
                        view.findViewById<View>(R.id.LessonsSchefuleRecyclingView).visibility =
                            View.VISIBLE
                        view.findViewById<View>(R.id.noScheduleItem).visibility = View.GONE
                    }
                }
            }
        }
    }

    fun checkIfToday(year: Int, month: Int, dayOfMonth: Int) {
        // Получаем текущую дату
        val today = Calendar.getInstance()

        // Сравниваем переданную дату с текущей
        isToday = (year == today.get(Calendar.YEAR) &&
                month == today.get(Calendar.MONTH) &&
                dayOfMonth == today.get(Calendar.DAY_OF_MONTH))
    }
}