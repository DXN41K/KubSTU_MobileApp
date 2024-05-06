package com.example.kubstu_app.ViewModel.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.LessonModel
import com.example.kubstu_app.Model.LessonsDomain
import com.example.kubstu_app.R

class HomeLessonsAdapter(val context: Context) : ListAdapter<LessonModel, HomeLessonsAdapter.LessonsViewHolder>(Comparator()) {
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
            timeTV.text = data.GetThisTime()
            audienceTV.text = data.audience
            nameTV.text = data.name
            teacher.text = data.teacher
            hasLeft.text = data.hasLeft
            altTime.text = "${data.pair} пара"
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

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val data = ConvertToVM(getItem(position))
        holder.bind(data)
    }

    private fun ConvertToVM(Smth: LessonModel): LessonsDomain {
        return LessonsDomain(
            Smth.kindofnagr.kindofnagr_name,
            Smth.classroom,
            Smth.disc.disc_name,
            Smth.teacher,
            Smth.pair
        )
    }

    class Comparator : DiffUtil.ItemCallback<LessonModel>() {
        override fun areItemsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
            return oldItem.pair == newItem.pair
        }

        override fun areContentsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
            return oldItem == newItem
        }

    }
}