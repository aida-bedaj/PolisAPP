package com.example.polisapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.polisapp.uilogic.course.CourseActivity
import com.example.polisapp.uilogic.student.StudentActivity
import com.example.polisapp.uilogic.teacher.TeacherActivity

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        findViewById<Button>(R.id.btnCourses).setOnClickListener {
            startActivity(Intent(this, CourseActivity::class.java))
        }

        findViewById<Button>(R.id.btnStudents).setOnClickListener {
            startActivity(Intent(this, StudentActivity::class.java))
        }

        findViewById<Button>(R.id.btnTeachers).setOnClickListener {
            startActivity(Intent(this, TeacherActivity::class.java))
        }
    }
}
