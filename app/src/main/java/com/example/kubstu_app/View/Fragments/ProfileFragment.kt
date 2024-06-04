package com.example.kubstu_app.View.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.kubstu_app.R
import com.example.kubstu_app.View.ChooseGroupActivity
import com.example.kubstu_app.View.LogInActivity
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.E


class ProfileFragment : Fragment() {

    private lateinit var TextInp: TextInputLayout


    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)
        TextInp = view.findViewById(R.id.GroupTBSettings)

        val ExitBtn = view.findViewById<Button>(R.id.ExitBtn)
        ExitBtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val logInIntent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(logInIntent)
            requireActivity().finish()
        }

        val SettingsChancheGroupBtn = view.findViewById<Button>(R.id.SettingsChancheGroupBtn)
        SettingsChancheGroupBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChooseGroupActivity::class.java)
            startActivityForResult(intent, LogInActivity.REQUEST_CODE)
        }

        val buttonWithVkLink = view.findViewById<ImageButton>(R.id.SettingsVkBtn)
            buttonWithVkLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://vk.com/kubstu_official")
            startActivity(intent)
        }

        val buttonWithTgLink = view.findViewById<ImageButton>(R.id.SettingsTgBtn)
            buttonWithTgLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://t.me/kubstu_official")
            startActivity(intent)
        }

        val buttonWithOkLink = view.findViewById<ImageButton>(R.id.SettingsOkBtn)
            buttonWithOkLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://ok.ru/group/70000005806787")
            startActivity(intent)
        }

        val buttonWithRtLink = view.findViewById<ImageButton>(R.id.SettingsRtBtn)
            buttonWithRtLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://rutube.ru/channel/25147184/")
            startActivity(intent)
        }

        val buttonWithSiteLink = view.findViewById<Button>(R.id.SettingsSiteBtn)
        buttonWithSiteLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://kubstu.ru/")
                startActivity(intent)
            }

        val buttonWithPerSiteLink = view.findViewById<Button>(R.id.SettingsPersonalSiteBtn)
        buttonWithPerSiteLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://student.kubstu.ru/")
            startActivity(intent)
        }

        val result = sharedPreferences.getString("Users_group", null)

        if (result == null){
            TextInp.editText?.setText("Ошибка")
        }
        else{
            TextInp.editText?.setText("${result}")
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == LogInActivity.REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
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