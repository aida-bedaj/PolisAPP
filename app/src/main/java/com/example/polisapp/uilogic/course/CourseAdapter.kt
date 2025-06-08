package com.example.polisapp.uilogic.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.model.CourseDTO

class CourseAdapter(private var courses: MutableList<CourseDTO>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

        var onDeleteClick: ((CourseDTO) -> Unit)? = null

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

        holder.itemView.findViewById<Button>(R.id.btnDeleteCourse).setOnClickListener {
            onDeleteClick?.invoke(course)
        }
    }
    override fun getItemCount(): Int = courses.size

    // 🔄 Add this for filtering or resetting
    fun updateList(newCourses: MutableList<CourseDTO>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }

    // 🔄 Add this for pagination
    fun addMoreCourses(moreCourses: List<CourseDTO>) {
        val start = courses.size
        courses.addAll(moreCourses)
        notifyItemRangeInserted(start, moreCourses.size)
    }

}
