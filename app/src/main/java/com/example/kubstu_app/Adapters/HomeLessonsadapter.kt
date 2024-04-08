package com.example.kubstu_app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Domains.LessonsDomain
import com.example.kubstu_app.R

class HomeLessonsadapter(private val lessons: List<LessonsDomain>, val context: Context)
    : RecyclerView.Adapter<HomeLessonsadapter.LessonsViewHolder>() {
    inner class LessonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val titleTextView: TextView = itemView.findViewById(R.id.SpecialNoImgTitle)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.SpecialNoImgSubtitle)
//        private val backGround: ImageView = itemView.findViewById(R.id.BackgroundImageView)
        fun bind(data: LessonsDomain) {
//            backGround.setImageResource(data.Background)
//            titleTextView.text = data.Title
//            descriptionTextView.text = data.SubTitle
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