package com.example.polisapp.uilogic.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.model.CourseDTO

class CourseAdapter(private val courses: List<CourseDTO>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textCourseName)
        val description: TextView = itemView.findViewById(R.id.textCourseDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.name.text = course.name
        holder.description.text = course.description
    }

    override fun getItemCount(): Int = courses.size
}
