package com.example.polisapp.uilogic.course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.api.CourseAPI
import com.example.polisapp.model.CourseDTO
import com.example.polisapp.uicomponents.SearchBarView
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.launch

class CourseActivity : AppCompatActivity() {

    private lateinit var courseApi: CourseAPI
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBarView
    private lateinit var adapter: CourseAdapter

    private var fullCourseList: List<CourseDTO> = emptyList()
    private var filteredCourseList: List<CourseDTO> = emptyList()

    // Pagination state
    private var currentPage = 0
    private val pageSize = 10
    private var isLoading = false
    private var isSearchMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        recyclerView = findViewById(R.id.recyclerCourses)
        searchBar = findViewById(R.id.searchBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        courseApi = ApiClient.retrofit.create(CourseAPI::class.java)
        adapter = CourseAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // 🔹 ADD COURSE BUTTON
        findViewById<Button>(R.id.btnAddCourse).setOnClickListener {
            val intent = Intent(this, AddCourseActivity::class.java)
            startActivity(intent)
        }

        setupDeleteHandler()
        fetchCourses()
        setupPaginationScrollListener()
        setupSearchListener()
    }

    private fun fetchCourses() {
        lifecycleScope.launch {
            try {
                fullCourseList = courseApi.getAllCourses()
                filteredCourseList = fullCourseList
                resetPagination()
                loadNextPage()
            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
                Toast.makeText(this@CourseActivity, "Failed to load courses", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupPaginationScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isSearchMode && lastVisibleItem + 3 >= totalItemCount) {
                    loadNextPage()
                }
            }
        })
    }

    private fun setupSearchListener() {
        searchBar.onSearchTextChanged = { query ->
            if (query.isNotBlank()) {
                isSearchMode = true
                val filtered = fullCourseList.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
                adapter.updateList(filtered.toMutableList())
            } else {
                isSearchMode = false
                filteredCourseList = fullCourseList
                resetPagination()
                adapter.updateList(mutableListOf())
                loadNextPage()
            }
        }
    }

    private fun setupDeleteHandler() {
        adapter.onDeleteClick = { course ->
            AlertDialog.Builder(this)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete '${course.name}'?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            courseApi.deleteCourse(course.id)
                            Toast.makeText(this@CourseActivity, "Course deleted", Toast.LENGTH_SHORT).show()
                            fetchCourses()
                        } catch (e: Exception) {
                            Toast.makeText(this@CourseActivity, "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun resetPagination() {
        currentPage = 0
        isLoading = false
    }

    private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, filteredCourseList.size)

        if (start < end) {
            isLoading = true
            val nextPage = filteredCourseList.subList(start, end)
            adapter.addMoreCourses(nextPage)
            currentPage++
            isLoading = false
        }
    }

    override fun onResume() {
        super.onResume()
        fetchCourses()
    }
}
