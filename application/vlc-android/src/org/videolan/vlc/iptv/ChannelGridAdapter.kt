package org.videolan.vlc.iptv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.videolan.vlc.R

/**
 * Adapter pour afficher les chaînes en grille
 */
class ChannelGridAdapter(
    private val channels: List<IPTVChannel>,
    private val onChannelClick: (IPTVChannel) -> Unit,
    private val onFavoriteClick: (IPTVChannel) -> Unit
) : RecyclerView.Adapter<ChannelGridAdapter.ChannelGridViewHolder>() {

    inner class ChannelGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.channelLogo)
        val name: TextView = itemView.findViewById(R.id.channelName)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)

        fun bind(channel: IPTVChannel) {
            name.text = channel.name
            
            // Charger le logo
            Glide.with(itemView.context)
                .load(channel.logo)
                .placeholder(R.drawable.ic_default_channel)
                .error(R.drawable.ic_default_channel)
                .into(logo)
            
            // Icône favori
            favoriteIcon.setImageResource(
                if (channel.isFavorite) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )
            
            // Clicks
            itemView.setOnClickListener {
                onChannelClick(channel)
            }
            
            favoriteIcon.setOnClickListener {
                onFavoriteClick(channel)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelGridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel_card, parent, false)
        
        // Ajuster la largeur pour la grille
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        val displayMetrics = parent.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth - 64) / 3  // 3 colonnes avec marges
        layoutParams.width = itemWidth
        view.layoutParams = layoutParams
        
        return ChannelGridViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelGridViewHolder, position: Int) {
        holder.bind(channels[position])
    }

    override fun getItemCount() = channels.size
}
