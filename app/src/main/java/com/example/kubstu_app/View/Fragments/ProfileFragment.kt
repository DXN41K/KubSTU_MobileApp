package com.example.kubstu_app.View.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.kubstu_app.Model.dic.SpecsModel
import com.example.kubstu_app.Model.dic.SpecsResponse
import com.example.kubstu_app.Model.student.Semester
import com.example.kubstu_app.Model.timeTable.Products
import com.example.kubstu_app.R
import com.example.kubstu_app.View.ChooseGroupActivity
import com.example.kubstu_app.View.LogInActivity
import com.example.kubstu_app.ViewModel.Retrofit.IAPI
import com.example.kubstu_app.ViewModel.Retrofit.retrofitClient
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.Locale
import kotlin.math.E


class ProfileFragment : Fragment() {

    private lateinit var TextInp: TextInputLayout


    private lateinit var sharedPreferences: SharedPreferences

    lateinit var list: Map<String, Semester>
    lateinit var specific: SpecsResponse
    lateinit var specific_text: SpecsModel
    lateinit var retro: Retrofit
    lateinit var lessonAPI: IAPI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        val access_token = sharedPreferences.getString("access_token", null)

        retrofitClient.Initialize(requireContext())
        retro = retrofitClient.getClient()
        lessonAPI = retro.create(IAPI::class.java)

        val spec_id = sharedPreferences.getInt("spec_id", 0)
        val kurs = sharedPreferences.getInt("kurs", 0)
        val spec = view.findViewById<TextView>(R.id.ProfileCard_Specific)
        val name = sharedPreferences.getString("imja", null)
        val Surname = sharedPreferences.getString("fam", null)
        val otch = sharedPreferences.getString("otch", null)

        val nameView = view.findViewById<TextView>(R.id.ProfileCard_Name)
        nameView.text = "$Surname\n$name $otch"

        lifecycleScope.launch(Dispatchers.IO) {
            if (access_token != null) {
                try {
                    list = lessonAPI.getGrades(access_token)
                    specific = lessonAPI.getSpecs()
                    specific_text = specific.data.first { it.id == (spec_id ?: 0) }


                    var count = 0
                    var sum = 0


                    if (!list.isNullOrEmpty()) {
                        for ((_, item) in list) {
                            if (!item.e.isNullOrEmpty()) {
                                for (i in item.e) {
                                    if (i.oc_f != 0 && i.oc_f != 8) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                            if (!item.z.isNullOrEmpty()) {
                                for (i in item.z) {
                                    if (i.tipkontr_id != 2 && (i.oc_f != 0 && i.oc_f != 8)) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                        }
                    }


                    var mid = sum.toDouble() / count.toDouble()
                    val roundedResult = String.format(Locale.US, "%.1f", mid)

                    requireActivity().runOnUiThread {
                        spec.text = "${kurs} курс\n${specific_text.code} ${specific_text.name}"
                        val midTxt = view.findViewById<TextView>(R.id.ProfileMidGradeTXT)
                        midTxt.text = roundedResult.toString()
                    }
                }
                // если возникает ошибка обновления токена, значит токен пользователь не авторизован
                catch (e: Exception) {
                    Log.e("ff", "ff is failed due to ${e.message}")
                }
            }
        }
        val profileLoadedWorksBtn = view.findViewById<CardView>(R.id.ProfileLoadedWorksBtn)

//        profileLoadedWorksBtn.setOnClickListener {
//            // Показать тост при нажатии
//            Toast.makeText(requireContext(), "Функция в разработке", Toast.LENGTH_SHORT).show()
//        }


//        val profileProtectAppBtn = view.findViewById<CardView>(R.id.ProfileProtectAppBtn)
//
//        profileProtectAppBtn.setOnClickListener {
//            // Показать тост при нажатии
//            Toast.makeText(requireContext(), "Функция в разработке", Toast.LENGTH_SHORT).show()
//        }

        val profileSMBtn = view.findViewById<CardView>(R.id.ProfileSMBtn)

        profileSMBtn.setOnClickListener {
            // Показать тост при нажатии
            Toast.makeText(requireContext(), "Переход по ссылке \"Официальные источники КубГТУ\"", Toast.LENGTH_SHORT).show()
        }

        val profilePrivacyBtn = view.findViewById<CardView>(R.id.ProfilePrivacyBtn)

        profilePrivacyBtn.setOnClickListener {
            // Показать тост при нажатии
            Toast.makeText(requireContext(), "Переход по ссылке \"Условия и Политика конфидециальности\"", Toast.LENGTH_SHORT).show()
        }

        val profileShareBtn = view.findViewById<CardView>(R.id.ProfileShareBtn)

        profileShareBtn.setOnClickListener {
            // Показать тост при нажатии
            // Создаём текст для отправки
            val shareText =
                "Попробуй это отличное приложение! Скачать можно по ссылке: https://kubstu.ru/s-836"

            // Создаем интент для действия "Поделиться"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            val chooserIntent =
                Intent.createChooser(shareIntent, "Поделитесь этим приложением").apply {
                    // Добавляем заголовок через EXTRA_TITLE
                    putExtra(Intent.EXTRA_TITLE, "Выберите приложение")
                }
            startActivity(chooserIntent)
        }

        val ExitBtn = view.findViewById<Button>(R.id.ExitBtn)
        ExitBtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val logInIntent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(logInIntent)
            requireActivity().finish()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == LogInActivity.REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val result = data?.getStringExtra("result")

            if (result == null) return
            else {
                val editor = sharedPreferences.edit()
                editor.putString("Users_group", "${result}")
                editor.apply()
                TextInp.hint = ""
                TextInp.editText?.setText(result)
            }
        }
    }
}