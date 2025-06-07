package com.example.polisapp.api

import com.example.polisapp.model.CourseDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface CourseAPI {
    @GET("/courses")
    suspend fun getAllCourses(): List<CourseDTO>

    @POST("/courses")
    suspend fun createCourse(@Body course: CourseDTO): CourseDTO
}