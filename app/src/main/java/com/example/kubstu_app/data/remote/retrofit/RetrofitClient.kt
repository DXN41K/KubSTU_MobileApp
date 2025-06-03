package com.example.kubstu_app.data.remote.retrofit

import android.content.Context
import com.example.kubstu_app.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitClient {

    private lateinit var http: HttpLoggingInterceptor
    private lateinit var clientBuilder: OkHttpClient.Builder
    private lateinit var okHttpClient: OkHttpClient
    fun Initialize(context: Context)
    {
        http = HttpLoggingInterceptor()
        http.level = HttpLoggingInterceptor.Level.BODY

        clientBuilder = OkHttpClient.Builder()

        val cf = CertificateFactory.getInstance("X.509")
        val cert = context.resources.openRawResource(R.raw.kubsturu)

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
        }
        finally {
            if (cert != null) {
                cert.close()
            }
        }


        okHttpClient = clientBuilder.addInterceptor(http).build()
    }
    private const val BASE_URL = "https://elkaf.kubstu.ru/api/"

    fun getClient(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}