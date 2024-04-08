package com.example.kubstu_app.Fragments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class HomeSpecialAdapter(private val specials: List<String>) : Adapter<HomeSpecialAdapter.SpecialViewHolder>() {

    class SpecialViewHolder(itemView: View) : ViewHolder(itemView) {
//        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
//        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return specials.size
    }

    override fun onBindViewHolder(holder: SpecialViewHolder, position: Int) {

    }

}