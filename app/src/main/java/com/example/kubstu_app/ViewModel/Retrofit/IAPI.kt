package com.example.kubstu_app.ViewModel.Retrofit

import com.example.kubstu_app.Model.timeTable.GroupList
import com.example.kubstu_app.Model.student.LoginResponse
import com.example.kubstu_app.Model.student.Semester
import com.example.kubstu_app.Model.student.UserResponse
import com.example.kubstu_app.Model.student.Work
import com.example.kubstu_app.Model.timeTable.Products
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IAPI {

    /**
     * Запрашивает расписание на сегодня по группе, уч. году, семестру.
     * @param gr Группа.
     * @param ugod Уч. год.
     * @param semestr Уч. год.
     * @return Products - модель данных.
     */
    @GET("timetable/today")
    suspend fun getNow(
        @Query("gr") gr: String,
        @Query("ugod") year: Int,
        @Query("semestr") sem: Int) : Products

    /**
     * Запрашивает расписание на указанный промежуток времени по группе, уч. году, семестру, начальной даты и конечной даты.
     * @param gr Группа.
     * @param ugod Уч. год.
     * @param semestr Уч. год.
     * @param date_start  Начальная дата.
     * @param date_end Конечная дата.
     * @return Products - модель данных.
     */
    @GET("timetable/schedule")
    suspend fun getThen(
        @Query("gr") gr: String,
        @Query("ugod") year: Int,
        @Query("semestr") sem: Int,
        @Query("date_start") start: String,
        @Query("date_end") end: String) : Products

    /**
     * Запрашивает список групп на заданный уч. год.
     * @param ugod Уч. год.
     * @return GroupList - модель данных.
     */
    @GET("timetable/gr-list")
    suspend fun getGroups(
        @Query("ugod") year: Int
    ) : GroupList

    /**
     * Отправляет данные для авторизации на сервер.
     * @param username Логин.
     * @param password Пароль.
     * @return LoginResponse - модель данных.
     */
    @FormUrlEncoded
    @POST("student/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    /**
     * Обновляет токен доступа с помощью токена обновления.
     * @param refreshToken Токен обновления.
     * @return LoginResponse - модель данных.
     */
    @POST("student/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): LoginResponse

    //
    /**
     * Проверка авторизации и получение текущего пользователя по токену доступа.
     * @param accessToken Токен доступа.
     * @return UserResponse - модель данных.
     */
    @POST("student/me")
    suspend fun getCurrentUser(
        @Header("Authorization") accessToken: String
    ): UserResponse

    /**
     * Запрашивает список оценок по токену доступа.
     * @param accessToken Токен доступа.
     * @return Map<String, Semester> - модель данных.
     */
    @GET("student/sess")
    suspend fun getGrades(
        @Header("Authorization") accessToken: String
    ): Map<String, Semester>

    /**
     * Запрашивает список загруженных работ по токену доступа.
     * @param accessToken Токен доступа.
     * @return List<Work>- модель данных.
     */
    @GET("student/work-list")
    suspend fun getUploadedWorks(
        @Header("Authorization") accessToken: String
    ): List<Work>
}