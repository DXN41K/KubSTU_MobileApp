package com.example.kubstu_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.kubstu_app.Adapters.HomeLessonsadapter
import com.example.kubstu_app.Adapters.HomeSpecialAdapter
import com.example.kubstu_app.Domains.LessonsDomain
import com.example.kubstu_app.Domains.SpecialDomain
import com.example.kubstu_app.R
import java.time.LocalDateTime


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val specRecyclerView: RecyclerView = view.findViewById(R.id.SpecialRecyclingView)

        val specdata = InitSpecialRecyclingView()

        //Важно!!! без этого не отображалось!!
        specRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val specadapter = HomeSpecialAdapter(specdata, requireContext())
        specRecyclerView.adapter = specadapter


        val LessRecyclerView: RecyclerView = view.findViewById(R.id.LessonsRecyclingView)

        val lessdata = InitLessonsRecyclingView()

        //Важно!!! без этого не отображалось!!
        LessRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val lessadapter = HomeLessonsadapter(lessdata, requireContext())
        LessRecyclerView.adapter = lessadapter
        return view
    }
    private fun InitSpecialRecyclingView() :List<SpecialDomain>{
        val specialItems = mutableListOf<SpecialDomain>()
        specialItems.add(SpecialDomain("Скидка 10% на печать", "до 05.04, действует\nодин раз", R.drawable.backgroundgradient_blue))
        specialItems.add(SpecialDomain("Встреча с работодателями", "01.04 в 11:00\nК-234", R.drawable.backgroundgradient_green))
        specialItems.add(SpecialDomain("Example 3", "Текст", R.drawable.backgroundgradient_yellow))
        specialItems.add(SpecialDomain("Example 4", "Текст", R.drawable.backgroundgradient_red))
        return specialItems
    }
    private fun InitLessonsRecyclingView() :List<LessonsDomain>{
        val lessonsItems = mutableListOf<LessonsDomain>()
        val currentTime = LocalDateTime.now()
        lessonsItems.add(LessonsDomain("Лекция", LocalDateTime.of(currentTime.year, currentTime.month, currentTime.dayOfMonth, 15, 0),
            "К-202", "Современные проблемы искусственного интеллекта", "Марков Виталий Николаевич"))
        lessonsItems.add(LessonsDomain("Другое", LocalDateTime.of(currentTime.year, currentTime.month, currentTime.dayOfMonth, 8, 20),
            "К-143", "Встреча с директором", ""))
        lessonsItems.add(LessonsDomain("Лабораторные занятия", LocalDateTime.of(currentTime.year, currentTime.month, currentTime.dayOfMonth, 13, 20),
            "К-202", "Современные проблемы искусственного интеллекта", "Марков Виталий Николаевич"))
        return lessonsItems
    }
}