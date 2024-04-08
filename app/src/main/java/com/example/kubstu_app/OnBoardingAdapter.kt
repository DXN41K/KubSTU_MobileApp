package com.example.kubstu_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnBoardingAdapter(val onBoardingItems: List<OnBoardingItem>): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    override fun getItemCount(): Int = onBoardingItems.size

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int)
    = holder.setOnBoardingData(onBoardingItems[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent, false
            )
        )
    }



    class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle = itemView.findViewById<TextView>(R.id.TextOnBoarding)
        private val imageOnBoarding = itemView.findViewById<ImageView>(R.id.ImageOnBoarding)

        fun setOnBoardingData(onBoardingItem: OnBoardingItem) {
            textTitle.text = onBoardingItem._title
            imageOnBoarding.setImageResource(onBoardingItem._image)
        }
    }

}