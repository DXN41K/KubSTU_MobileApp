package com.example.kubstu_app.Model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Year

interface IAPI {
    @GET("timetable/today")
    suspend fun GetNow(
        @Query("gr") gr: String,
        @Query("ugod") year: Int,
        @Query("semestr") sem: Int) : Products
}