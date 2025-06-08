package com.example.polisapp.uilogic.student

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polisapp.R
import com.example.polisapp.api.StudentAPI
import com.example.polisapp.model.StudentDTO
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button
    private lateinit var studentApi: StudentAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        nameInput = findViewById(R.id.editStudentName)
        emailInput = findViewById(R.id.editStudentEmail)
        saveButton = findViewById(R.id.btnSaveStudent)

        studentApi = ApiClient.retrofit.create(StudentAPI::class.java)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = StudentDTO(name = name, email = email)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    studentApi.createStudent(student)
                    runOnUiThread {
                        Toast.makeText(this@AddStudentActivity, "Student added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@AddStudentActivity, "Failed to save student", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
