package org.videolan.vlc.iptv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.videolan.vlc.R

/**
 * Adapter pour afficher les catégories et leurs chaînes
 */
class CategoryAdapter(
    private val categories: List<IPTVCategory>,
    private val onChannelClick: (IPTVChannel) -> Unit,
    private val onFavoriteClick: (IPTVChannel) -> Unit,
    private val onSeeAllClick: (IPTVCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitle)
        private val seeAllButton: TextView = itemView.findViewById(R.id.seeAllButton)
        private val channelsRecyclerView: RecyclerView = itemView.findViewById(R.id.channelsRecyclerView)

        fun bind(category: IPTVCategory) {
            categoryTitle.text = category.name
            
            // Afficher seulement les 3 premières chaînes
            val displayChannels = category.channels.take(3)
            
            // Setup en grille 3 colonnes au lieu de horizontal
            channelsRecyclerView.layoutManager = GridLayoutManager(itemView.context, 3)
            
            val adapter = ChannelGridAdapter(
                displayChannels,
                onChannelClick,
                onFavoriteClick
            )
            channelsRecyclerView.adapter = adapter
            
            // Click sur "Voir tout"
            seeAllButton.setOnClickListener {
                onSeeAllClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_section, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}
