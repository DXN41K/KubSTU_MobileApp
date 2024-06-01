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
                R.id.settings -> replaceFragment(ProfileFragment())
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

