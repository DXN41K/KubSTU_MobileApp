package com.example.kubstu_app.View.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.IAPI
import com.example.kubstu_app.Model.Products
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.HomeLessonsAdapter
import com.example.kubstu_app.ViewModel.Adapters.ScheduleLessonsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.Calendar
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class ScheduleFragment : Fragment() {

    private lateinit var view: View
    lateinit var lessadapter: ScheduleLessonsAdapter
    lateinit var spinner: Spinner


    var currYear: Int = 0
    var DuplcurrYear: Int = 0
    var currMonth: Int = 0
    var currDay: Int = 0

    val monthToString = mapOf(
        0 to "Январь",
        1 to "Февраль",
        2 to "Март",
        3 to "Апрель",
        4 to "Май",
        5 to "Июнь",
        6 to "Июль",
        7 to "Август",
        8 to "Сентябрь",
        9 to "Октбрь",
        10 to "Ноябрь",
        11 to "Декабрь"
    )
    val groups = arrayOf("21-КБ-ПР1", "21-КБ-ПР2")

    var selectedGroup =groups[0]

    var list: Products? = null
    lateinit var retro: Retrofit
    lateinit var client: OkHttpClient
    lateinit var lessonAPI: IAPI


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val http = HttpLoggingInterceptor()
        http.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()

        val cf = CertificateFactory.getInstance("X.509")
        val cert = requireContext().resources.openRawResource(R.raw.kubsturu)

        try {
            val ca = cf.generateCertificate(cert)
            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            val tmfAlgo = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgo)
            tmf.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)

            clientBuilder.sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
        } finally {
            if (cert != null) {
                cert.close()
            }
        }

        client = clientBuilder.addInterceptor(http).build()

        retro = Retrofit.Builder().baseUrl("https://elkaf.kubstu.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        lessonAPI = retro.create(IAPI::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_schedule, container, false)

        //настраиваем обработчик выбора даты
        val monthText: TextView = view.findViewById(R.id.month)

        val currentDate = Calendar.getInstance()
        currDay = currentDate.get(Calendar.DAY_OF_MONTH)
        currMonth = currentDate.get(Calendar.MONTH)
        currYear = currentDate.get(Calendar.YEAR)
        if (currMonth < 8) DuplcurrYear = currYear - 1

        monthText.text = "$currDay, ${monthToString[currMonth]}"

        //настраиваем выпадающее меню
        spinner = view.findViewById(R.id.GroupSpinner)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, groups)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)



        //настраиваем recycling view

        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsSchefuleRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lessadapter = ScheduleLessonsAdapter(requireContext())
        LessRecyclerView.adapter = lessadapter



        CoroutineScope(Dispatchers.IO).launch {
            try {
                list = lessonAPI.GetThen(selectedGroup, DuplcurrYear, 2, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
            }
            catch (e: Exception) {
            }
            if (isAdded){
                requireActivity().runOnUiThread {
                    lessadapter.submitList(list?.data?.sortedBy { it.pair })
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
                            list = lessonAPI.GetThen(selectedGroup, DuplcurrYear, 2, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
                        }
                        catch (e: Exception) {
                        }
                        if (isAdded){
                            requireActivity().runOnUiThread {
                                lessadapter.submitList(list?.data?.sortedBy { it.pair })
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        spinner.onItemSelectedListener = itemSelectedListener

        val monthText: TextView = view.findViewById(R.id.month)

        monthText.setOnClickListener{
            showCal()
        }
    }
    private fun showCal() {
        //получение сущности календаря
        val cal = Calendar.getInstance()


        val dateListener = DatePickerDialog.OnDateSetListener{ _, year, month, dayofmonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayofmonth)

            val monthText = view.findViewById<TextView>(R.id.month)

            this.currYear = year
            this.currMonth = month
            this.currDay = dayofmonth
            monthText.text = "$dayofmonth, ${monthToString[month]}"
            if (currMonth < 8) this.DuplcurrYear = currYear - 1

            //обработка выбранной даты будет здесь
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    list = lessonAPI.GetThen(selectedGroup, DuplcurrYear, 2, "$currYear-${currMonth+1}-$currDay", "$currYear-${currMonth+1}-$currDay")
                }
                catch (e: Exception) {
                }
                if (isAdded){
                    requireActivity().runOnUiThread {
                        lessadapter.submitList(list?.data?.sortedBy { it.pair })
                    }
                }
            }
        }

        val datePicker = DatePickerDialog(requireContext(),
            dateListener, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        //установка максимальной даты для текущего семестра
        cal.set(Calendar.YEAR, 2024)
        cal.set(Calendar.MONTH, Calendar.JUNE)
        cal.set(Calendar.DAY_OF_MONTH, 9)

        //установка даты в календарь выбора
        val maxDateInMillis = cal.timeInMillis
        datePicker.datePicker.maxDate = maxDateInMillis

        //аналогично для минимальной даты
        cal.set(Calendar.YEAR, 2024)
        cal.set(Calendar.MONTH, Calendar.FEBRUARY)
        cal.set(Calendar.DAY_OF_MONTH, 5)

        val minDateInMillis = cal.timeInMillis
        datePicker.datePicker.minDate = minDateInMillis

        datePicker.show()
    }

}