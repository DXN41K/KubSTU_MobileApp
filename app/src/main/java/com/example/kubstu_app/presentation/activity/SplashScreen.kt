package com.example.kubstu_app.presentation.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kubstu_app.data.remote.response.LoginResponse
import com.example.kubstu_app.R
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SplashScreen : AppCompatActivity() {

    // задержка при загрузке экрана
    private val SPLASH_DELAY: Long = 500

    private lateinit var _user: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // создаем/получаем преференсы
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            "Users_preferences",
            Context.MODE_PRIVATE
        )

        // создаем класс-синглтон ретрофит, для запросов
        RetrofitClient.Initialize(this)
        val api = RetrofitClient.getClient()
        val lessonAPI = api.create(IAPI::class.java)

        // получение сохраненного токена обновления
        val refreshToken = sharedPreferences.getString("refresh_token", null)

        // корутина обновления токена
        lifecycleScope.launch(Dispatchers.IO) {
            // если токен есть то, пытаемся обновить его. в случае успеха, мы убеждаемся, что
            // пользователь авторизован
            if (refreshToken != null) {
                try {
                    _user = lessonAPI.refreshToken(refreshToken)

                    Log.i(
                        "Login", "id: ${_user.id} \n" +
                                "${_user.fam} \n" +
                                "${_user.imja} \n" +
                                "${_user.otch} \n" +
                                "gr: ${_user.gr} \n" +
                                "formaob_id: ${_user.formaob_id} \n" +
                                "inst_id: ${_user.inst_id} \n" +
                                "kurs: ${_user.kurs} \n" +
                                "spec_id: ${_user.spec_id} \n" +
                                "AT: ${_user.access_token} \n" +
                                "RT: ${_user.refresh_token} \n"
                    )

                    sharedPreferences.edit().apply {
                        putString("access_token", _user.access_token)
                        putString("refresh_token", _user.refresh_token)
                        putString("fam", _user.fam)
                        putString("imja", _user.imja)
                        putString("otch", _user.otch)
                        putString("gr", _user.gr)
                        putInt("kurs", _user.kurs)
                        putInt("spec_id", _user.spec_id)
                        putInt("inst_id", _user.inst_id)
                        putInt("formaob_id", _user.formaob_id)
                        apply()
                    }
                    withContext(Dispatchers.Main) {
                        enterApp()
                    }
                }
                // если возникает ошибка обновления токена, значит токен пользователь не авторизован
                catch (e: HttpException) {
                    Log.e("Login", "Login is failed due to ${e.message}")
                    withContext(Dispatchers.Main) {
                        sharedPreferences.edit().clear().apply()
                        denyAccess()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("Login", "Login is failed due to ${e.message}")

                        // Выводим тост о проблеме с соединением
                        Toast.makeText(applicationContext, "Проблема с соединением", Toast.LENGTH_LONG).show()

                        // Закрываем приложение
                        finish()

                        // Если хотите завершить работу всего приложения, не только активити:
                        finishAffinity()
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

            // Закрываем активити SplashScreenActivity, чтобы пользователь не мог вернуться к ней
            // по нажатию кнопки "назад"
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

