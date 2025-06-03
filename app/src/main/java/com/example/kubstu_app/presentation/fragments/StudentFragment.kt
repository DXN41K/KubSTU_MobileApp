package com.example.kubstu_app.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.kubstu_app.data.model.SpecsModel
import com.example.kubstu_app.data.remote.response.SpecsResponse
import com.example.kubstu_app.data.model.SemesterModel
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Adapters.GradesViewPagerWithRecyclerAdapter
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.time.LocalDate
import java.util.Locale


class StudentFragment : Fragment() {


    private lateinit var sharedPreferences: SharedPreferences

    lateinit var list: Map<String, SemesterModel>
    lateinit var Splittedlist: List<Map<String, SemesterModel>>
    lateinit var specific: SpecsResponse
    lateinit var specific_text: SpecsModel
    lateinit var retro: Retrofit
    lateinit var lessonAPI: IAPI

    val tips_kontr = listOf(2,3,4,9,10,11,12)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        val access_token = sharedPreferences.getString("access_token", null)

        RetrofitClient.Initialize(requireContext())
        retro = RetrofitClient.getClient()
        lessonAPI = retro.create(IAPI::class.java)

        val spec_id = sharedPreferences.getInt("spec_id", 0)
        val kurs = sharedPreferences.getInt("kurs", 0)
        val spec = view.findViewById<TextView>(R.id.StudentCard_Specific)
        val name = sharedPreferences.getString("imja", null)
        val Surname = sharedPreferences.getString("fam", null)
        val otch = sharedPreferences.getString("otch", null)

        val viewPager = view.findViewById<ViewPager2>(R.id.GradesVP)
        val tabLayout = view.findViewById<TabLayout>(R.id.GradesTabLayout)
        val titles: MutableList<String> = mutableListOf()


        val nameView = view.findViewById<TextView>(R.id.StudentCard_Name)
        nameView.text = "$Surname\n$name $otch"

        lifecycleScope.launch(Dispatchers.IO) {
            if (access_token != null) {
                try {
                    list = lessonAPI.getGrades(access_token)
                    specific = lessonAPI.getSpecs()
                    specific_text = specific.data!!.first { it.id == spec_id }

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

                    Splittedlist = splitMapIntoPairs(list)

                    var x = Splittedlist.size

                    if (LocalDate.now().monthValue >= 9) {

                        if (x == 0) {
                            titles.add("${LocalDate.now().year}/${LocalDate.now().year + 1} уч. год")
                        }

                        while (x > 0) {
                            titles.add("${LocalDate.now().year - x + 1}/${LocalDate.now().year - x + 2} уч. год")
                            x--
                        }
                    }
                    else {
                        if (x == 0) {
                            titles.add("${LocalDate.now().year - 1}/${LocalDate.now().year} уч. год")
                        }

                        while (x > 0) {
                            titles.add("${LocalDate.now().year - x}/${LocalDate.now().year - x + 1} уч. год")
                            x--
                        }
                    }

                    var mid = sum.toDouble() / count.toDouble()
                    val roundedResult = String.format(Locale.US, "%.1f", mid)

                    withContext(Dispatchers.Main) {
                        spec.text = "$kurs курс\n${specific_text.code} ${specific_text.name}"
                        val midTxt = view.findViewById<TextView>(R.id.StudentMidGradeTXT)
                        if (!list.isNullOrEmpty()) {
                            midTxt.text = roundedResult.toString()
                        }
                        val adapter = GradesViewPagerWithRecyclerAdapter(Splittedlist)
                        viewPager.adapter = adapter
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            tab.text = titles[position] // Установка названия таба
                        }.attach()
                    }
                }
                // если возникает ошибка обновления токена, значит токен пользователь не авторизован
                catch (e: Exception) {
                    Log.e("Login", "Login is failed due to ${e.message}")
                }
                if (isAdded) {
                    requireActivity().runOnUiThread {
                    }
                }
            }
        }

        return view
    }

    fun splitMapIntoPairs(map: Map<String, SemesterModel>): List<Map<String, SemesterModel>> {
        val result = mutableListOf<Map<String, SemesterModel>>()
        var tempMap = mutableMapOf<String, SemesterModel>()

        if (map.isNullOrEmpty()) return emptyList()

        for ((key, value) in map) {
            tempMap[key] = value

            if (tempMap.size == 2) {
                result.add(tempMap.toMap())
                tempMap = mutableMapOf()
            }
        }

        if (tempMap.isNotEmpty()) {
            result.add(tempMap.toMap())
        }

        return result
    }
}
