package com.example.polisapp.model

data class StudentDTO(
    val id: Long = 0,
    val name: String,
    val email: String,
    val courseId: Long? = null // can be null if no course
)