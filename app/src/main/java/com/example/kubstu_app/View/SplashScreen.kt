package com.example.kubstu_app.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.kubstu_app.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val sharedPreferences: SharedPreferences = getSharedPreferences("Users_preferences", Context.MODE_PRIVATE);
        val value: String? = sharedPreferences.getString("Users_group", null);
        if (value != null) {

            // Используем Handler для задержки перехода на главный экран
            Handler().postDelayed({

                // Создаем интент для перехода на главный экран
                val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(mainIntent)

                // Закрываем активити SplashScreenActivity, чтобы пользователь не мог вернуться к ней по нажатию кнопки "назад"
                finish()

            }, SPLASH_DELAY)

        } else {

            Handler().postDelayed({
                val logInIntent = Intent(this@SplashScreen, LogInActivity::class.java)
                startActivity(logInIntent)
                finish()
            }, SPLASH_DELAY)

        }


    }
}