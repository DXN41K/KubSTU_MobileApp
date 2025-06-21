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
import com.example.kubstu_app.data.model.SemesterModel
import com.example.kubstu_app.data.model.SpecsModel
import com.example.kubstu_app.data.remote.response.SpecsResponse
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Locale

class SplashScreen : AppCompatActivity() {

    // задержка при загрузке экрана
    private val SPLASH_DELAY: Long = 500

    lateinit var list: Map<String, SemesterModel>
    lateinit var specific: SpecsResponse
    lateinit var specific_text: SpecsModel
    private lateinit var _user: LoginResponse

    val tips_kontr = listOf(2, 3, 4, 9, 10, 11, 12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences: SharedPreferences = getSharedPreferences(
            "Users_preferences",
            Context.MODE_PRIVATE
        )

        val api = RetrofitClient.getClient()
        val lessonAPI = api.create(IAPI::class.java)

        val refreshToken = sharedPreferences.getString("refresh_token", null)

        lifecycleScope.launch(Dispatchers.IO) {
            if (refreshToken != null) {
                try {
                    _user = lessonAPI.refreshToken(refreshToken)

                    list = lessonAPI.getGrades(_user.access_token)
                    specific = lessonAPI.getSpecs()
                    specific_text = specific.data!!.first { it.id == (_user.spec_id ?: 0) }

                    var count = 0
                    var sum = 0

                    if (!list.isNullOrEmpty()) {
                        for ((_, item) in list) {
                            if (!item.e.isNullOrEmpty()) {
                                for (i in item.e) {
                                    if (!tips_kontr.contains(i.tipkontr_id)) continue
                                    if (i.oc_f != 0 && i.oc_f != 8) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                            if (!item.z.isNullOrEmpty()) {
                                for (i in item.z) {
                                    if (!tips_kontr.contains(i.tipkontr_id)) continue
                                    if (i.tipkontr_id != 2 && (i.oc_f != 0 && i.oc_f != 8)) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                        }
                    }
                    var roundedResult = "-"
                    if (count > 0) {
                        var mid = sum.toDouble() / count.toDouble()
                        roundedResult = String.format(Locale.US, "%.1f", mid)
                    }

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
                                "RT: ${_user.refresh_token} \n" +
                                "average_grade: ${_user.refresh_token} \n" +
                                "specific_name: ${_user.refresh_token} \n" +
                                "specific_code: ${_user.refresh_token} \n"
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
                        putString("average_grade", roundedResult)
                        putString("specific_code", specific_text.code)
                        putString("specific_name", specific_text.name)
                        apply()
                    }
                    withContext(Dispatchers.Main) {
                        enterApp()
                    }
                } catch (e: HttpException) {
                    Log.e("Login", "Login is failed due to ${e.message}")
                    withContext(Dispatchers.Main) {
                        sharedPreferences.edit().clear().apply()
                        denyAccess()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("Login", "Login is failed due to ${e.message}")

                        // Выводим тост о проблеме с соединением
                        Toast.makeText(
                            applicationContext,
                            "Проблема с соединением",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        finishAffinity()
                    }
                }
            } else {
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
            val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(mainIntent)
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

