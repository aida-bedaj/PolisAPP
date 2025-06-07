package com.example.polisapp.uilogic.teacher

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.api.TeacherAPI
import com.example.polisapp.model.TeacherDTO
import com.example.polisapp.uicomponents.SearchBarView
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.launch

class TeacherActivity : AppCompatActivity() {

    private lateinit var teacherApi: TeacherAPI
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBarView
    private lateinit var adapter: TeacherAdapter

    private var fullList: List<TeacherDTO> = emptyList()
    private var filteredList: List<TeacherDTO> = emptyList()
    private var currentPage = 0
    private val pageSize = 10
    private var isLoading = false
    private var isSearchMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        recyclerView = findViewById(R.id.recyclerTeachers)
        searchBar = findViewById(R.id.searchBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TeacherAdapter(mutableListOf())
        recyclerView.adapter = adapter
        teacherApi = ApiClient.retrofit.create(TeacherApi::class.java)

        fetchTeachers()
        setupScroll()
        setupSearch()
    }

    private fun fetchTeachers() {
        lifecycleScope.launch {
            try {
                fullList = teacherApi.getAllTeachers()
                filteredList = fullList
                resetPagination()
                loadNextPage()
            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
                Toast.makeText(this@TeacherActivity, "Failed to load teachers", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupScroll() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && !isSearchMode && lm.findLastVisibleItemPosition() + 3 >= lm.itemCount) {
                    loadNextPage()
                }
            }
        })
    }

    private fun setupSearch() {
        searchBar.onSearchTextChanged = { query ->
            if (query.isNotBlank()) {
                isSearchMode = true
                val filtered = fullList.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.department.contains(query, ignoreCase = true)
                }
                adapter.updateList(filtered.toMutableList())
            } else {
                isSearchMode = false
                filteredList = fullList
                resetPagination()
                adapter.updateList(mutableListOf())
                loadNextPage()
            }
        }
    }

    private fun resetPagination() {
        currentPage = 0
        isLoading = false
    }

    private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, filteredList.size)
        if (start < end) {
            isLoading = true
            val nextPage = filteredList.subList(start, end)
            adapter.addMoreTeachers(nextPage)
            currentPage++
            isLoading = false
        }
    }
}
