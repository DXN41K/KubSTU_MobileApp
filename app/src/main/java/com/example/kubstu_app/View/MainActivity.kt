package com.example.kubstu_app.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kubstu_app.R
import com.example.kubstu_app.View.Fragments.HomeFragment
import com.example.kubstu_app.View.Fragments.ProfileFragment
import com.example.kubstu_app.View.Fragments.ScheduleFragment
import com.example.kubstu_app.View.Fragments.StudentFragment
import com.example.kubstu_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener() {
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.schedule -> replaceFragment(ScheduleFragment())
                R.id.student -> replaceFragment(StudentFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            return@setOnItemSelectedListener true
        }



        val intent = Intent(this, LogInActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Обработка результата, полученного от второго активити
            val returnedValue = data?.getStringExtra("key")
            // Далее вы можете выполнить нужные действия с полученным результатом
        }
        else {
            finish()
        }
    }
    companion object {
        const val REQUEST_CODE = 123
    }
}

