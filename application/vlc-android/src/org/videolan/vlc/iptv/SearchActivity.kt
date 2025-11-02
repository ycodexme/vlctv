package org.videolan.vlc.iptv

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.videolan.vlc.R
import org.videolan.vlc.gui.video.VideoPlayerActivity

/**
 * Activité de recherche de chaînes IPTV
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var iptvManager: IPTVManager
    private lateinit var toolbar: Toolbar
    private lateinit var searchEditText: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        iptvManager = IPTVManager(this)
        
        toolbar = findViewById(R.id.toolbar)
        searchEditText = findViewById(R.id.searchEditText)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        setupToolbar()
        setupSearchInput()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupSearchInput() {
        searchEditText.requestFocus()
        
        // Recherche avec debounce (attendre 300ms après la dernière frappe)
        searchEditText.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300) // Debounce
                val query = text.toString().trim()
                if (query.length >= 2) {
                    performSearch(query)
                } else if (query.isEmpty()) {
                    clearResults()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        searchResultsRecyclerView.layoutManager = GridLayoutManager(this, 3)
        
        // Ajouter un espacement entre les éléments (12dp)
        val spacing = (12 * resources.displayMetrics.density).toInt()
        searchResultsRecyclerView.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
    }

    private suspend fun performSearch(query: String) {
        try {
            val results = iptvManager.searchChannels(query)
            displayResults(results)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayResults(channels: List<IPTVChannel>) {
        val adapter = ChannelGridAdapter(
            channels,
            onChannelClick = { channel -> playChannel(channel) },
            onFavoriteClick = { channel -> toggleFavorite(channel) }
        )
        searchResultsRecyclerView.adapter = adapter
    }

    private fun clearResults() {
        searchResultsRecyclerView.adapter = null
    }

    private fun playChannel(channel: IPTVChannel) {
        val uri = Uri.parse(channel.sourceUrl)
        VideoPlayerActivity.start(this, uri, channel.name)
    }

    private fun toggleFavorite(channel: IPTVChannel) {
        iptvManager.toggleFavorite(channel)
        // Refresh la recherche
        searchEditText.text?.let { text ->
            val query = text.toString().trim()
            if (query.length >= 2) {
                lifecycleScope.launch {
                    performSearch(query)
                }
            }
        }
    }
    
    /**
     * ItemDecoration pour ajouter un espacement dans une grille
     */
    private class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {
        
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}
