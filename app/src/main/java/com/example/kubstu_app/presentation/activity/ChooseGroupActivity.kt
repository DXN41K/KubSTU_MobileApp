package com.example.kubstu_app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.data.remote.response.GroupResponse
import com.example.kubstu_app.data.model.GroupModel
import com.example.kubstu_app.data.remote.retrofit.IAPI
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

    private var originalGroupList: List<GroupModel> = emptyList()
    private lateinit var searchEditText: EditText
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var lessonAPI: IAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group)

        searchEditText = findViewById(R.id.searchEditText)
        val GroupRecyclerView: RecyclerView = findViewById(R.id.GroupRV)

        GroupRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        groupAdapter = GroupAdapter(this, this)
        GroupRecyclerView.adapter = groupAdapter

        setupApiService()
        setupSearch()
        fetchGroups()
    }

    private fun setupApiService() {
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

            clientBuilder.sslSocketFactory(
                sslContext.socketFactory,
                tmf.trustManagers[0] as X509TrustManager
            )
        } finally {
            cert.close()
        }

        val client = clientBuilder.addInterceptor(http).build()

        val retro = Retrofit.Builder().baseUrl("https://elkaf.kubstu.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        // --- ИЗМЕНЕНИЕ: Инициализируем lessonAPI здесь, как член класса ---
        lessonAPI = retro.create(IAPI::class.java)
    }


    private fun fetchGroups() {
        var list: GroupResponse? = null
        if (!::lessonAPI.isInitialized) {
            // В случае ошибки инициализации (что маловероятно после onCreate)
            // можно логировать или показать сообщение
            return
        }
        lifecycleScope.launch(Dispatchers.IO) { // ИЗМЕНЕНИЕ: Указываем Dispatchers.IO для сетевых операций
            try {
                // --- ИЗМЕНЕНИЕ: Используем lessonAPI, который был инициализирован в setupApiService() ---
                val response = lessonAPI.getGroups(2024)
                response?.data?.let { groups ->
                    // --- КЛЮЧЕВОЕ ИЗМЕНЕНИЕ: Заполняем originalGroupList данными с сервера ---
                    originalGroupList = groups.sortedBy { it.name }

                    // --- ИЗМЕНЕНИЕ: Возвращаемся в главный поток для обновления UI ---
                    // И добавляем проверки состояния Activity и адаптера
                    runOnUiThread {
                        if (!isFinishing && ::groupAdapter.isInitialized) {
                            groupAdapter.submitList(originalGroupList)
                        }
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибки (например, показ Toast, логирование ошибки)
                e.printStackTrace()
                runOnUiThread {
                    // Опционально: показать сообщение пользователю об ошибке загрузки
                    // Toast.makeText(this@ChooseGroupActivity, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterGroups(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun filterGroups(query: String) {
        val filteredList = if (query.isBlank()) {
            originalGroupList
        } else {
            val lowerCaseQuery = query.lowercase()
            originalGroupList.filter { group ->
                group.name.lowercase().contains(lowerCaseQuery) ||
                        (groupAdapter.InstNames[group.inst_id]?.lowercase()
                            ?.contains(lowerCaseQuery) == true) ||
                        group.kurs.toString().contains(lowerCaseQuery) ||
                        (groupAdapter.efNames[group.formaob_id]?.lowercase()
                            ?.contains(lowerCaseQuery) == true)
            }
        }
        if (::groupAdapter.isInitialized) {
            groupAdapter.submitList(filteredList)
        }
    }

    override fun onClick(group: GroupModel) {
        val intent = Intent()
        intent.putExtra("result", group.name)
        setResult(RESULT_OK, intent)
        finish()
    }

}
