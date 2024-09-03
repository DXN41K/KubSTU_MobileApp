package com.example.kubstu_app.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.timeTable.GroupList
import com.example.kubstu_app.Model.timeTable.GroupModel
import com.example.kubstu_app.ViewModel.Retrofit.IAPI
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.GroupAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class ChooseGroupActivity : AppCompatActivity(), GroupAdapter.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group)

        val GroupRecyclerView: RecyclerView = findViewById(R.id.GroupRV)

        //Важно!!! без этого не отображалось!!
        GroupRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val groupadapter = GroupAdapter(this, this)
        GroupRecyclerView.adapter = groupadapter



        val http = HttpLoggingInterceptor()
        http.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()

        val cf = CertificateFactory.getInstance("X.509")
        val cert = this.resources.openRawResource(R.raw.kubsturu)

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

        var list: GroupList? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {

                list = lessonAPI.getGroups(2023)
            }
            catch (e: Exception) {
            }

                runOnUiThread {
                    groupadapter.submitList(list?.data?.sortedBy { it.name })
                }
            }
        }

    override fun onClick(group: GroupModel) {
        val intent = Intent()
        intent.putExtra("result", group.name)
        setResult(RESULT_OK, intent)
        finish()
    }

}
