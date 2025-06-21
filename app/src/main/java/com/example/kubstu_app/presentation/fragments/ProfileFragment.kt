package com.example.kubstu_app.presentation.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.kubstu_app.data.model.SemesterModel
import com.example.kubstu_app.R
import com.example.kubstu_app.presentation.activity.LogInActivity

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    lateinit var list: Map<String, SemesterModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        val roundedResult = sharedPreferences.getString("average_grade", "-")
        val specific_code = sharedPreferences.getString("specific_code", "-")
        val specific_name = sharedPreferences.getString("specific_name", "-")
        val kurs = sharedPreferences.getInt("kurs", 0)
        val spec = view.findViewById<TextView>(R.id.ProfileCard_Specific)
        val name = sharedPreferences.getString("imja", null)
        val Surname = sharedPreferences.getString("fam", null)
        val otch = sharedPreferences.getString("otch", null)

        val nameView = view.findViewById<TextView>(R.id.ProfileCard_Name)
        nameView.text = "$Surname\n$name $otch"

        spec.text = "${kurs} курс\n${specific_code} ${specific_name}"
        val midTxt = view.findViewById<TextView>(R.id.ProfileMidGradeTXT)
        midTxt.text = roundedResult.toString()

        val profileSMBtn = view.findViewById<CardView>(R.id.ProfileSMBtn)
        profileSMBtn.setOnClickListener {
            val url = "https://taplink.cc/kubstu"

            // Создаем Intent с ACTION_VIEW
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            startActivity(intent)
        }

        val profilePrivacyBtn = view.findViewById<CardView>(R.id.ProfilePrivacyBtn)
        profilePrivacyBtn.setOnClickListener {
            val url = "https://kubstu.ru/s-637"

            // Создаем Intent с ACTION_VIEW
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            startActivity(intent)
        }

        val profileShareBtn = view.findViewById<CardView>(R.id.ProfileShareBtn)
        profileShareBtn.setOnClickListener {
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

//        val profileLoadedWorksBtn = view.findViewById<CardView>(R.id.ProfileLoadedWorksBtn)
//
//        profileLoadedWorksBtn.setOnClickListener {
//            // Показать тост при нажатии
//            Toast.makeText(requireContext(), "Функция в разработке", Toast.LENGTH_SHORT).show()
//        }
//
//
//        val profileProtectAppBtn = view.findViewById<CardView>(R.id.ProfileProtectAppBtn)
//
//        profileProtectAppBtn.setOnClickListener {
//            // Показать тост при нажатии
//            Toast.makeText(requireContext(), "Функция в разработке", Toast.LENGTH_SHORT).show()
//        }

        return view
    }

}