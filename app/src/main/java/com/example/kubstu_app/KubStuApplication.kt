package com.example.kubstu_app

import android.app.Application
import com.example.kubstu_app.data.local.AppDatabase
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import retrofit2.Retrofit

class KubStuApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val retrofit: Retrofit by lazy {
        RetrofitClient.getClient() // Здесь вызываем метод для получения уже настроенного Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.Initialize(this)
    }

}