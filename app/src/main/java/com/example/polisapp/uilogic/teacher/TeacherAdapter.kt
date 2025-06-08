package com.example.polisapp.uilogic.teacher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.model.TeacherDTO

class TeacherAdapter(private var teachers: MutableList<TeacherDTO>) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

        var onDeleteClick: ((TeacherDTO) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textTeacherName)
        val department: TextView = itemView.findViewById(R.id.textTeacherDept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teacher = teachers[position]
        holder.name.text = teacher.name
        holder.department.text = teacher.department

        val deleteButton = holder.itemView.findViewById<Button>(R.id.btnDeleteTeacher)
        deleteButton.setOnClickListener {
            onDeleteClick?.invoke(teacher)
        }
    }

    override fun getItemCount(): Int = teachers.size

    fun addMoreTeachers(more: List<TeacherDTO>) {
        val start = teachers.size
        teachers.addAll(more)
        notifyItemRangeInserted(start, more.size)
    }

    fun updateList(newList: MutableList<TeacherDTO>) {
        teachers.clear()
        teachers.addAll(newList)
        notifyDataSetChanged()
    }
}
