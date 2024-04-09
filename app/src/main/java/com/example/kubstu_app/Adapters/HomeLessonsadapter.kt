package com.example.kubstu_app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Domains.LessonsDomain
import com.example.kubstu_app.R
import java.time.LocalDateTime

class HomeLessonsadapter(private val lessons: List<LessonsDomain>, val context: Context)
    : RecyclerView.Adapter<HomeLessonsadapter.LessonsViewHolder>() {
    inner class LessonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTV: TextView = itemView.findViewById(R.id.LessonType)
        val timeTV: TextView = itemView.findViewById(R.id.LessonsTime)
        val audienceTV: TextView = itemView.findViewById(R.id.audience)
        val nameTV: TextView = itemView.findViewById(R.id.LessonName)
        val teacher: TextView = itemView.findViewById(R.id.LessonTeacher)
        val hasLeft: TextView = itemView.findViewById(R.id.LessonStarts)
        val altTime: TextView = itemView.findViewById(R.id.LessonsCount)
        val colorCard: ConstraintLayout = itemView.findViewById(R.id.LessonCardCornerColor)
        fun bind(data: LessonsDomain) {
            typeTV.text = data.type
            timeTV.text = data.getTime()
            audienceTV.text = data.audience
            nameTV.text = data.name
            teacher.text = data.teacher
            hasLeft.text = data.hasLeft
            altTime.text = data.altTime
            colorCard.setBackgroundColor(
                when (data.type) {
                    ("Лекция") -> context.resources.getColor(R.color.HomeLection)
                    ("Лабораторные занятия") -> context.resources.getColor(R.color.HomeLab)
                    ("Практические занятия") -> context.resources.getColor(R.color.HomePract)
                    else -> context.resources.getColor(R.color.HomeElse)
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_lessonshome, parent, false)
        return LessonsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val data = lessons[position]
        holder.bind(data)
    }
}