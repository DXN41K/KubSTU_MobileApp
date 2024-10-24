package com.example.kubstu_app.ViewModel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kubstu_app.Model.SpecialDomain
import com.example.kubstu_app.R

class HomeSpecialAdapter(private val specials: List<SpecialDomain>, val context: Context) : Adapter<ViewHolder>() {
    companion object {
        private const val TYPE_WITH_IMAGE = 1
        private const val TYPE_WITHOUT_IMAGE = 0
    }

    inner class TextSpecialViewHolder(itemView: View) : ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.SpecialNoImgTitle)
        //private val descriptionTextView: TextView = itemView.findViewById(R.id.SpecialNoImgSubtitle)
        private val backGround: ImageView = itemView.findViewById(R.id.BackgroundNoImageView)
        fun bind(data: SpecialDomain) {
            backGround.setImageResource(data.Background)
            titleTextView.text = data.Title
//            descriptionTextView.text = data.SubTitle
        }
    }
    inner class PicSpecialViewHolder(itemView: View) : ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.SpecialImgTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.SpecialImgSubtitle)
        private val backGround: ImageView = itemView.findViewById(R.id.BackgroundImageView)
        private val image: ImageView = itemView.findViewById(R.id.SpecialImage)
        fun bind(data: SpecialDomain) {
            backGround.setImageResource(data.Background)
            titleTextView.text = data.Title
            descriptionTextView.text = data.SubTitle
            image.setImageDrawable(ContextCompat.getDrawable(context, data.Image!!))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_WITH_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_specialhome2_pic, parent, false)
                PicSpecialViewHolder(view)
            }
            TYPE_WITHOUT_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_specialhome2, parent, false)
                TextSpecialViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        when (holder) {
            is PicSpecialViewHolder -> {
                holder.bind(specials[position])
            }
            is TextSpecialViewHolder -> {
                holder.bind(specials[position])
            }
            else -> throw IllegalArgumentException("Invalid holder type")
        }

    override fun getItemCount(): Int = specials.size
    override fun getItemViewType(position: Int): Int = if (specials[position].Image != null) TYPE_WITH_IMAGE else TYPE_WITHOUT_IMAGE
}