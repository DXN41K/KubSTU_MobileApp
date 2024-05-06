package com.example.kubstu_app.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.IAPI
import com.example.kubstu_app.Model.Products
import com.example.kubstu_app.Model.SpecialDomain
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.HomeLessonsAdapter
import com.example.kubstu_app.ViewModel.Adapters.HomeSpecialAdapter
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
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class HomeFragment : Fragment() {

    lateinit var lessadapter: HomeLessonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val specRecyclerView: RecyclerView = view.findViewById(R.id.SpecialRecyclingView)

        val specdata = InitSpecialRecyclingView()

        //Важно!!! без этого не отображалось!!
        specRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val specadapter = HomeSpecialAdapter(specdata, requireContext())
        specRecyclerView.adapter = specadapter




        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsRecyclingView)

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        lessadapter = HomeLessonsAdapter(requireContext())
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

        var list: Products? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                list = lessonAPI.GetNow("21-КБ-ПР1", 2023, 2)
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

    private fun InitSpecialRecyclingView() :List<SpecialDomain>{
        val specialItems = mutableListOf<SpecialDomain>()
        specialItems.add(SpecialDomain("Встреча с работодателями", "01.04 в 11:00\nК-234", R.drawable.backgroundgradient_blue))
        specialItems.add(SpecialDomain("Скидка 10% на печать", "до 05.04, действует\nодин раз", R.drawable.backgroundgradient_green, R.drawable.imgpercent))
        specialItems.add(SpecialDomain("Example 3", "Текст", R.drawable.backgroundgradient_yellow))
        specialItems.add(SpecialDomain("Example 4", "Текст", R.drawable.backgroundgradient_red))
        return specialItems
    }

}