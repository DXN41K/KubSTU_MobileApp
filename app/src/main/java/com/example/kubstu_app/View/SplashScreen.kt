package com.example.kubstu_app.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kubstu_app.Model.student.LoginResponse
import com.example.kubstu_app.Model.student.UserResponse
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Retrofit.IAPI
import com.example.kubstu_app.ViewModel.Retrofit.retrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {

    // задержка при загрузке экрана
    private val SPLASH_DELAY: Long = 2000

    private lateinit var user: UserResponse
    private lateinit var tokens: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // создаем/получаем преференсы
        val sharedPreferences: SharedPreferences = getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        // создаем класс-синглтон ретрофит, для запросов
        retrofitClient.Initialize(this)
        val api = retrofitClient.getClient()
        val lessonAPI = api.create(IAPI::class.java)

        // получение сохраненного токена обновления
        val refreshToken = sharedPreferences.getString("refresh_token", null)


        // корутина обновления токена
        lifecycleScope.launch(Dispatchers.IO) {
            // если токен есть то, пытаемся обновить его. в случае успеха, мы убеждаемся, что
            // пользователь авторизован
            if (refreshToken != null) {
                try {
                    tokens = lessonAPI.refreshToken(refreshToken)
                    sharedPreferences.edit().apply {
                        putString("access_token", tokens.access_token)
                        putString("refresh_token", tokens.refresh_token)
                        putString("fam", tokens.fam)
                        putString("imja", tokens.imja)
                        putString("otch", tokens.otch)
                        putString("gr", tokens.gr)
                        putInt("kurs", tokens.kurs)
                        putInt("inst_id", tokens.inst_id)
                        putInt("formaob_id", tokens.formaob_id)
                        apply()
                    }
                    withContext(Dispatchers.Main) {
                        enterApp()
                    }
                }
                // если возникает ошибка обновления токена, значит токен пользователь не авторизован
                catch (e: Exception) {
                    Log.e("Login", "Login is failed due to ${e.message}")
                    withContext(Dispatchers.Main) {
                        sharedPreferences.edit().clear().apply()
                        denyAccess()
                    }
                }
            }

            // если токена нет, то вывод на экран авторизации
            else {
                withContext(Dispatchers.Main) {
                    sharedPreferences.edit().clear().apply()
                    denyAccess()
                }
            }
        }
    }

    // метод для входа на основной экран
    private fun enterApp() {

        // Используем Handler для задержки перехода на главный экран
        Handler().postDelayed({

            // Создаем интент для перехода на главный экран
            val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(mainIntent)

            // Закрываем активити SplashScreenActivity, чтобы пользователь не мог вернуться к ней по нажатию кнопки "назад"
            finish()

        }, SPLASH_DELAY)
    }

    // метод для перехода на экран авторизации
    private fun denyAccess() {
        Handler().postDelayed({
            val logInIntent = Intent(this@SplashScreen, LogInActivity::class.java)
            startActivity(logInIntent)
            finish()
        }, SPLASH_DELAY)
    }
}

//                val response = lessonAPI.getCurrentUser("Ha-8Yrk6UCvN2e-o0CvhgzG_YlEbEwTa_1725116201")
//                Log.i("Login", "id: ${response.id}, \n" +
//                        "FIO: ${response.fam}, " +
//                        "${response.imja}, " +
//                        "${response.otch}, \n" +
//                        "Group: ${response.gr}, \n" +
//                        "Course: ${response.kurs}, \n" +
//                        "inst_id: ${response.inst_id}, \n" +
//                        "formfob: ${response.formaob_id}, \n")

//
