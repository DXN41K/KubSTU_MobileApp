package com.example.kubstu_app.ViewModel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.LessonModel
import com.example.kubstu_app.Model.LessonsDomain
import com.example.kubstu_app.R

class ScheduleLessonsAdapter(val context: Context) :
    ListAdapter<LessonModel, ScheduleLessonsAdapter.LessonsViewHolder>(Comparator()) {

    val colorsFromType = mapOf(
        "Лекции" to context.resources.getColor(R.color.HomeLection),
        "Лабораторные занятия" to context.resources.getColor(R.color.HomeLab),
        "Практические занятия" to context.resources.getColor(R.color.HomeLab)
    )
    val elseColor = context.resources.getColor(R.color.HomeElse)

    inner class LessonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTV: TextView = itemView.findViewById(R.id.LessonType)
        val timeTV: TextView = itemView.findViewById(R.id.LessonsTime)
        val audienceTV: TextView = itemView.findViewById(R.id.audience)
        val nameTV: TextView = itemView.findViewById(R.id.LessonName)
        val teacher: TextView = itemView.findViewById(R.id.LessonTeacher)
        val altTime: TextView = itemView.findViewById(R.id.LessonsCount)
        fun bind(data: LessonsDomain) {
            typeTV.text = data.type
            timeTV.text = data.GetThisTime()
            audienceTV.text = data.audience
            nameTV.text = data.name
            teacher.text = data.teacher
            altTime.text = "${data.pair}"
        }
        fun bindEmptyState() {
            val emptyTextView: TextView = itemView.findViewById(R.id.textView8)
            emptyTextView.text = "На сегодня занятий не найдено"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        return if (viewType == TYPE_EMPTY) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_emptyschedule, parent, false)
            LessonsViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_lessonsschedule, parent, false)
            return LessonsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val data = ConvertToVM(getItem(position))
            holder.bind(data)
        } else {
            holder.bindEmptyState()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) TYPE_EMPTY else TYPE_ITEM
    }

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_EMPTY = 0
    }

    private fun ConvertToVM(Smth: LessonModel): LessonsDomain {
        return LessonsDomain(
            Smth.kindofnagr.kindofnagr_name,
            Smth.classroom,
            Smth.disc.disc_name,
            Smth.teacher,
            Smth.pair,
            Smth.date
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

