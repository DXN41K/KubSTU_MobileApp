package com.example.kubstu_app.ViewModel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.student.Semester
import com.example.kubstu_app.R

class GradesViewPagerWithRecyclerAdapter(private val pagesData: List<Map<String, Semester>>) :
    RecyclerView.Adapter<GradesViewPagerWithRecyclerAdapter.PageViewHolder>() {

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView1: RecyclerView = itemView.findViewById(R.id.GradesRecyclerView1)
        val recyclerView2: RecyclerView = itemView.findViewById(R.id.GradesRecyclerView2)
        val springSem: LinearLayout = itemView.findViewById(R.id.SpringSem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grades_recyclingview_viewpager, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.recyclerView1.layoutManager = LinearLayoutManager(holder.recyclerView1.context)
        holder.recyclerView2.layoutManager = LinearLayoutManager(holder.recyclerView2.context)

        val pageData = pagesData[position]

        val gradesRecyclerAdapter1 = GradesRecyclerAdapter(pageData.entries.elementAt(0).value)
        holder.recyclerView1.adapter = gradesRecyclerAdapter1

        if (pageData.entries.size > 1) {
            val gradesRecyclerAdapter2 = GradesRecyclerAdapter(pageData.entries.elementAt(1).value)
            holder.recyclerView2.adapter = gradesRecyclerAdapter2
            holder.springSem.visibility = View.VISIBLE
        } else {
            holder.springSem.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return pagesData.size
    }
}