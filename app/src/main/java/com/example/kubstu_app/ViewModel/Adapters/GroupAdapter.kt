package com.example.kubstu_app.ViewModel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kubstu_app.Model.timeTable.GroupModel
import com.example.kubstu_app.R

class GroupAdapter(val context: Context, val listener: Listener) : ListAdapter<GroupModel, GroupAdapter.GroupViewHolder>(Comparator()) {

    val InstNames = mapOf(
        495 to "Институт нефти, газа и энергетики",
        516 to "Институт компьютерных систем и информационной безопасности",
        490 to "Институт пищевой и перерабатывающей промышленности",
        29 to "Институт экономики, управления и бизнеса",
        538 to "Институт строительства и транспортной инфраструктуры",
        539 to "Институт механики, робототехники, инженерии транспортных и технических систем",
        540 to "Институт фундаментальных наук",
        541 to "Инженерно-технологический колледж",
        34 to "Подготовительное отделение для иностранных граждан",
        50 to "Новороссийский политехнический институт",
        52 to "Армавирский механико-технологический институт"
    )

    val efNames = mapOf(
        1 to "Очная форма обучения",
        2 to "Очно-заочная форма обучения",
        3 to "Заочная форма обучения"
    )
    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.GroupName)
        val course: TextView = itemView.findViewById(R.id.Course)
        val instName: TextView = itemView.findViewById(R.id.InstName)
        val efName: TextView = itemView.findViewById(R.id.EFName)
        fun bind(data: GroupModel, listener: Listener) {
            groupName.text = data.name
            course.text = "${data.kurs} курс"
            efName.text = efNames[data.formaob_id]?: "Не указано"
            instName.text = InstNames[data.inst_id]?: "Не указано"
            itemView.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_groups, parent, false)
        return GroupViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, listener)
    }
    class Comparator : DiffUtil.ItemCallback<GroupModel>() {
        override fun areItemsTheSame(oldItem: GroupModel, newItem: GroupModel): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: GroupModel, newItem: GroupModel): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun onClick(group: GroupModel)
    }
}