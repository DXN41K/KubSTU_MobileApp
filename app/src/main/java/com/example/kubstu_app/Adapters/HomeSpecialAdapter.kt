package com.example.kubstu_app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kubstu_app.Domains.SpecialDomain
import com.example.kubstu_app.R

class HomeSpecialAdapter(private val specials: List<SpecialDomain>, val context: Context) : Adapter<HomeSpecialAdapter.TextSpecialViewHolder>() {
    companion object {
        const val TYPE_WITH_IMAGE = R.layout.viewholder_specialhome
        const val TYPE_WITHOUT_IMAGE = 2
    }
    inner class TextSpecialViewHolder(itemView: View) : ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.SpecialNoImgTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.SpecialNoImgSubtitle)
        private val backGround: ImageView = itemView.findViewById(R.id.BackgroundImageView)
        fun bind(data: SpecialDomain) {
            backGround.setImageResource(data.Background)
            titleTextView.text = data.Title
            descriptionTextView.text = data.SubTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextSpecialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_specialhome, parent, false)
        return TextSpecialViewHolder(view)
//            when (viewType) {
//            TYPE_WITH_IMAGE -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_with_image, parent, false)
//                ImageViewHolder(view)
//            }
//            TYPE_WITHOUT_IMAGE -> {
//            }
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
    }

    override fun getItemCount(): Int {
        return specials.size
    }

    override fun onBindViewHolder(holder: TextSpecialViewHolder, position: Int) {
        val data = specials[position]
        holder.bind(data)
//        when (holder) {
////            is ImageViewHolder -> {
////                holder.bind(data)
////            }
//            is TextSpecialViewHolder -> {
//                holder.bind(data)
//            }
        }
    }