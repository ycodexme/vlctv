package org.videolan.vlc.iptv

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.videolan.vlc.R
import org.videolan.vlc.gui.video.VideoPlayerActivity

/**
 * Activité principale pour l'interface IPTV
 */
class IPTVHomeActivity : AppCompatActivity() {

    private lateinit var iptvManager: IPTVManager
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iptv_home)

        iptvManager = IPTVManager(this)
        
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        bottomNav = findViewById(R.id.bottomNav)

        setupRecyclerView()
        setupBottomNavigation()
        loadChannels()
    }

    private fun setupRecyclerView() {
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Déjà sur l'accueil
                    true
                }
                R.id.nav_search -> {
                    // TODO: Ouvrir la recherche
                    true
                }
                R.id.nav_favorites -> {
                    // Afficher les favoris
                    showFavorites()
                    true
                }
                R.id.nav_settings -> {
                    // TODO: Ouvrir les paramètres
                    true
                }
                else -> false
            }
        }
    }

    private fun loadChannels() {
        progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val categories = iptvManager.loadCategories()
                displayCategories(categories)
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Afficher un message d'erreur
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayCategories(categories: List<IPTVCategory>) {
        val adapter = CategoryAdapter(
            categories,
            onChannelClick = { channel -> playChannel(channel) },
            onFavoriteClick = { channel -> toggleFavorite(channel) },
            onSeeAllClick = { category -> showCategoryDetail(category) }
        )
        categoriesRecyclerView.adapter = adapter
    }

    /**
     * Lance la lecture d'une chaîne directement
     */
    private fun playChannel(channel: IPTVChannel) {
        val uri = Uri.parse(channel.sourceUrl)
        VideoPlayerActivity.start(this, uri, channel.name)
    }

    /**
     * Toggle le statut favori d'une chaîne
     */
    private fun toggleFavorite(channel: IPTVChannel) {
        iptvManager.toggleFavorite(channel)
    }

    /**
     * Ouvre l'écran de détail d'une catégorie
     */
    private fun showCategoryDetail(category: IPTVCategory) {
        val intent = Intent(this, CategoryDetailActivity::class.java).apply {
            putExtra("category", category)
        }
        startActivity(intent)
    }

    /**
     * Affiche la liste des favoris
     */
    private fun showFavorites() {
        lifecycleScope.launch {
            val favorites = iptvManager.getFavoriteChannels()
            if (favorites.isNotEmpty()) {
                val category = IPTVCategory("Favoris", favorites)
                showCategoryDetail(category)
            }
        }
    }
}
