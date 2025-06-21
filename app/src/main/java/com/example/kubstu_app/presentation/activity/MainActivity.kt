package com.example.kubstu_app.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kubstu_app.R
import com.example.kubstu_app.presentation.fragments.HomeFragment
import com.example.kubstu_app.presentation.fragments.ProfileFragment
import com.example.kubstu_app.presentation.fragments.ScheduleFragment
import com.example.kubstu_app.presentation.fragments.StudentFragment
import com.example.kubstu_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        replaceFragment(HomeFragment())
        replaceFragment(ScheduleFragment())

        binding.bottomNavigationView.setOnItemSelectedListener() {
            when(it.itemId) {
//                R.id.home -> replaceFragment(HomeFragment())
                R.id.schedule -> replaceFragment(ScheduleFragment())
                R.id.grades -> replaceFragment(StudentFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            return@setOnItemSelectedListener true
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}

