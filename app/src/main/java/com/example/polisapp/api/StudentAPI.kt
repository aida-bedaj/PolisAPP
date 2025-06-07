package com.example.polisapp.api

import com.example.polisapp.model.StudentDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface StudentAPI {
    @GET("/students")
    suspend fun getAllStudents(): List<StudentDTO>

    @POST("/students")
    suspend fun createStudent(@Body student: StudentDTO): StudentDTO
}