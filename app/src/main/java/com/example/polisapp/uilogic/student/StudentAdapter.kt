package com.example.polisapp.uilogic.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.model.StudentDTO

class StudentAdapter(private var students: MutableList<StudentDTO>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textStudentName)
        val email: TextView = itemView.findViewById(R.id.textStudentEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.name.text = student.name
        holder.email.text = student.email
    }

    override fun getItemCount(): Int = students.size

    fun addMoreStudents(more: List<StudentDTO>) {
        val start = students.size
        students.addAll(more)
        notifyItemRangeInserted(start, more.size)
    }

    fun updateList(newList: MutableList<StudentDTO>) {
        students.clear()
        students.addAll(newList)
        notifyDataSetChanged()
    }
}
