package com.example.kubstu_app.ViewModel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.presentation.viewstate.OnBoardingViewState
import com.example.kubstu_app.R

class OnBoardingAdapter(val onBoardingViewStates: List<OnBoardingViewState>): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    override fun getItemCount(): Int = onBoardingViewStates.size

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int)
    = holder.setOnBoardingData(onBoardingViewStates[position])

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

        fun setOnBoardingData(onBoardingViewState: OnBoardingViewState) {
            textTitle.text = onBoardingViewState.title
            imageOnBoarding.setImageResource(onBoardingViewState.image)
        }
    }

}