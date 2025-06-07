package com.example.polisapp.api

import com.example.polisapp.model.TeacherDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface TeacherAPI {
    @GET("/teachers")
    suspend fun getAllTeachers(): List<TeacherDTO>

    @POST("/teachers")
    suspend fun createTeacher(@Body teacher: TeacherDTO): TeacherDTO
}