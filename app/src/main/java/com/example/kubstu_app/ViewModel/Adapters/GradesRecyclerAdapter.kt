package com.example.kubstu_app.ViewModel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.student.Exam
import com.example.kubstu_app.Model.student.Semester
import com.example.kubstu_app.Model.student.Zachet
import com.example.kubstu_app.R

class GradesRecyclerAdapter(private val items: Semester) :
    RecyclerView.Adapter<GradesRecyclerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.GradesName)
        val type: TextView = itemView.findViewById(R.id.GradesType)
        val grade: TextView = itemView.findViewById(R.id.GradesGrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_grades_exam, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var grades = (items?.e ?: emptyList()) + (items?.z ?: emptyList())
        val context = holder.itemView.context

        var title = ""
        var type = ""
        var color = 0
        var grade = ""

        val currItem = grades[position]
        when (currItem) {
            is Exam -> {
                title = currItem.disc_name
                type = "Экзамен"
                grade = when (currItem.oc_f) {
                    8 -> "-"
                    0 -> "2"
                    else -> currItem.oc_f.toString()
                }
                color = when (grade) {
                    "5" -> ContextCompat.getColor(context, R.color.NewColor_Green)
                    "4" -> ContextCompat.getColor(context, R.color.NewColor_Mint)
                    "3" -> ContextCompat.getColor(context, R.color.NewColor_Orange)
                    else -> ContextCompat.getColor(context, R.color.NewColor_Red)
                }
            }
            is Zachet -> {
                title = currItem.disc_name.replace(Regex("^\\S+\\s+\\d*\\s*"), "")
                type = when (currItem.tipkontr_id) {
                    2 -> "Зачет"
                    3 -> "Курсовой проект"
                    4 -> "Курсовая работа"
                    9 -> "Учебная практика"
                    10 -> "Производственная практика"
                    11 -> "Выпускная квалификационная работа"
                    12 -> "Государственный экзамен"
                    else -> "Другое"
                }


                grade = currItem.oc_f.toString()
                if (type == "Зачет" && grade == "3") {grade = "Зачет"}
                if (grade == "8") {grade = "-"}
                if (grade == "0") {grade = "2"}

                color = when (grade) {
                    "5" -> ContextCompat.getColor(context, R.color.NewColor_Green)
                    "4" -> ContextCompat.getColor(context, R.color.NewColor_Mint)
                    "3" -> ContextCompat.getColor(context, R.color.NewColor_Orange)
                    "Зачет" -> ContextCompat.getColor(context, R.color.NewColor_Blue)
                    else -> ContextCompat.getColor(context, R.color.NewColor_Red)
                }
            }
        }

        holder.title.text = title
        holder.type.text = type
        holder.grade.text = grade
        holder.grade.setTextColor(color)

    }

    override fun getItemCount(): Int {
        return (items?.e?.size ?: 0) + (items?.z?.size ?: 0)
    }
}