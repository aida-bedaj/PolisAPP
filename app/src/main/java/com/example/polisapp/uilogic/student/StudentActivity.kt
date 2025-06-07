package com.example.polisapp.uilogic.student

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polisapp.R
import com.example.polisapp.api.StudentAPI
import com.example.polisapp.model.StudentDTO
import com.example.polisapp.uicomponents.SearchBarView
import com.example.polisapp.util.ApiClient
import kotlinx.coroutines.launch

class StudentActivity : AppCompatActivity() {

    private lateinit var studentApi: StudentAPI
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBarView
    private lateinit var adapter: StudentAdapter

    private var fullList: List<StudentDTO> = emptyList()
    private var filteredList: List<StudentDTO> = emptyList()
    private var currentPage = 0
    private val pageSize = 10
    private var isLoading = false
    private var isSearchMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        recyclerView = findViewById(R.id.recyclerStudents)
        searchBar = findViewById(R.id.searchBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = StudentAdapter(mutableListOf())
        recyclerView.adapter = adapter
        studentApi = ApiClient.retrofit.create(StudentAPI::class.java)

        fetchStudents()
        setupScroll()
        setupSearch()
    }

    private fun fetchStudents() {
        lifecycleScope.launch {
            try {
                fullList = studentApi.getAllStudents()
                filteredList = fullList
                resetPagination()
                loadNextPage()
            } catch (e: Exception) {
                Log.e("API_ERROR", e.toString())
                Toast.makeText(this@StudentActivity, "Failed to load students", Toast.LENGTH_LONG).show()
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
                            it.email.contains(query, ignoreCase = true)
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
            adapter.addMoreStudents(nextPage)
            currentPage++
            isLoading = false
        }
    }
}
