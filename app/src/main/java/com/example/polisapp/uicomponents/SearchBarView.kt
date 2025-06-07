package com.example.polisapp.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.SearchView
import com.example.polisapp.R

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val searchView: SearchView

    var onSearchTextChanged: ((String) -> Unit)? = null

    init {
        inflate(context, R.layout.view_search_bar, this)
        searchView = findViewById(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                onSearchTextChanged?.invoke(newText ?: "")
                return true
            }
        })
    }
}