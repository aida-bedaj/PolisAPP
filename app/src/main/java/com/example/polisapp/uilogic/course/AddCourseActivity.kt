package com.example.polisapp.uilogic.course

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polisapp.R
import com.example.polisapp.api.CourseAPI
import com.example.polisapp.model.CourseDTO
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCourseActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var descInput: EditText
    private lateinit var saveButton: Button
    private lateinit var courseApi: CourseAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        nameInput = findViewById(R.id.editCourseName)
        descInput = findViewById(R.id.editCourseDesc)
        saveButton = findViewById(R.id.btnSaveCourse)

        courseApi = ApiClient.retrofit.create(CourseAPI::class.java)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val desc = descInput.text.toString().trim()

            if (name.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val course = CourseDTO(name = name, description = desc)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    courseApi.createCourse(course)
                    runOnUiThread {
                        Toast.makeText(this@AddCourseActivity, getString(R.string.course_added), Toast.LENGTH_SHORT).show()
                        finish() // return to previous activity
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@AddCourseActivity, "Failed to save course", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
