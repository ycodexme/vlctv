package org.videolan.vlc.iptv

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.videolan.vlc.R
import org.videolan.vlc.gui.video.VideoPlayerActivity

/**
 * Activité pour afficher toutes les chaînes d'une catégorie en grille
 */
class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var iptvManager: IPTVManager
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView
    private lateinit var channelsGridRecyclerView: RecyclerView
    private lateinit var adapter: ChannelGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        iptvManager = IPTVManager(this)
        
        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        channelsGridRecyclerView = findViewById(R.id.channelsGridRecyclerView)

        // Récupérer la catégorie depuis l'intent
        val category = intent.getParcelableExtra<IPTVCategory>("category")
        
        category?.let {
            setupToolbar(it.name)
            setupGridRecyclerView(it.channels)
        }
    }

    private fun setupToolbar(categoryName: String) {
        toolbarTitle.text = categoryName
        toolbar.setNavigationOnClickListener {
            finish()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupGridRecyclerView(channels: List<IPTVChannel>) {
        // Grille avec 3 colonnes
        channelsGridRecyclerView.layoutManager = GridLayoutManager(this, 3)
        
        // Ajouter un espacement entre les éléments (12dp)
        val spacing = (12 * resources.displayMetrics.density).toInt()
        channelsGridRecyclerView.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
        
        adapter = ChannelGridAdapter(
            channels,
            onChannelClick = { channel -> playChannel(channel) },
            onFavoriteClick = { channel -> toggleFavorite(channel) }
        )
        
        channelsGridRecyclerView.adapter = adapter
    }

    /**
     * Lance la lecture d'une chaîne
     */
    private fun playChannel(channel: IPTVChannel) {
        val uri = Uri.parse(channel.sourceUrl)
        VideoPlayerActivity.start(this, uri, channel.name)
    }

    /**
     * Toggle le statut favori et rafraîchit l'affichage
     */
    private fun toggleFavorite(channel: IPTVChannel) {
        iptvManager.toggleFavorite(channel)
        adapter.notifyDataSetChanged()
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
