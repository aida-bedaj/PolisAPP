package com.example.polisapp.uilogic.teacher

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polisapp.R
import com.example.polisapp.api.TeacherAPI
import com.example.polisapp.model.TeacherDTO
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTeacherActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var deptInput: EditText
    private lateinit var saveButton: Button
    private lateinit var teacherApi: TeacherAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)

        nameInput = findViewById(R.id.editTeacherName)
        deptInput = findViewById(R.id.editTeacherDept)
        saveButton = findViewById(R.id.btnSaveTeacher)

        teacherApi = ApiClient.retrofit.create(TeacherAPI::class.java)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val dept = deptInput.text.toString().trim()

            if (name.isEmpty() || dept.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val teacher = TeacherDTO(name = name, department = dept)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    teacherApi.createTeacher(teacher)
                    runOnUiThread {
                        Toast.makeText(this@AddTeacherActivity, "Teacher added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@AddTeacherActivity, "Failed to save teacher", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
