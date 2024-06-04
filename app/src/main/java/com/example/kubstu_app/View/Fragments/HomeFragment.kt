package com.example.kubstu_app.View.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.IAPI
import com.example.kubstu_app.Model.Products
import com.example.kubstu_app.Model.SpecialDomain
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.HomeLessonsAdapter
import com.example.kubstu_app.ViewModel.Adapters.HomeSpecialAdapter
import com.example.kubstu_app.ViewModel.Adapters.ScheduleLessonsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.Calendar
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class HomeFragment : Fragment() {

    lateinit var lessadapter: ScheduleLessonsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        val userNameTV = view.findViewById<TextView>(R.id.UserNametTV)
        val name: String? = sharedPreferences.getString("Users_name", null)
        val surname: String? = sharedPreferences.getString("Users_surname", null)

        userNameTV.text = if (name == null  || surname == null) "Пользователь" else "${name} ${surname}"

        val settingsServices = view.findViewById<CardView>(R.id.servicebtn)
        settingsServices.setOnClickListener {
            val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            navbar.selectedItemId = R.id.settings
        }

        val scheduleBtn = view.findViewById<ImageButton>(R.id.GoToScheduleBtn)
        scheduleBtn.setOnClickListener {
            val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            navbar.selectedItemId = R.id.schedule
        }


        view.findViewById<TextView>(R.id.GoodDayText).text =
            when (getCurrentTime()){
                in 4..11 -> "Доброе утро,"
                in 12..17 -> "Добрый день,"
                else -> "Добрый вечер,"
            }

        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lessadapter = ScheduleLessonsAdapter(requireContext())
        LessRecyclerView.adapter = lessadapter



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

        val client = clientBuilder.addInterceptor(http).build()

        val retro = Retrofit.Builder().baseUrl("https://elkaf.kubstu.ru/api/").client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val lessonAPI = retro.create(IAPI::class.java)

        var year = getCurrentYear()
        val month = getCurrentMonth()
        var sem = 1

        if (month < 9) {
            year--
            sem = 2
        }

        val group = sharedPreferences.getString("Users_group", null)

        var list: Products? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {

                list = lessonAPI.GetNow(group?:"", year, sem)
            }
            catch (e: Exception) {
            }
            if (isAdded){
            requireActivity().runOnUiThread {
                lessadapter.submitList(list?.data?.sortedBy { it.pair })
                if (lessadapter.itemCount == 0) view.findViewById<TextView>(R.id.HomeNoPairText).alpha = 1f
                else view.findViewById<TextView>(R.id.HomeNoPairText).alpha = 0f
            }
            }
        }

        return view
    }

    private fun InitSpecialRecyclingView() :List<SpecialDomain>{
        val specialItems = mutableListOf<SpecialDomain>()
        specialItems.add(SpecialDomain("Встреча с работодателями", "01.04 в 11:00\nК-234", R.drawable.backgroundgradient_blue))
        specialItems.add(SpecialDomain("Скидка 10% на печать", "до 05.04, действует\nодин раз", R.drawable.backgroundgradient_green, R.drawable.imgpercent))
        specialItems.add(SpecialDomain("Example 3", "Текст", R.drawable.backgroundgradient_yellow))
        specialItems.add(SpecialDomain("Example 4", "Текст", R.drawable.backgroundgradient_red))
        return specialItems
    }

    fun getCurrentTime(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }
    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH)
    }

}