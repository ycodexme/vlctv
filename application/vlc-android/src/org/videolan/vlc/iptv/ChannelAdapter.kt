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
 * Adapter pour afficher les chaînes dans un RecyclerView horizontal
 */
class ChannelAdapter(
    private val channels: List<IPTVChannel>,
    private val onChannelClick: (IPTVChannel) -> Unit,
    private val onFavoriteClick: (IPTVChannel) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    inner class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.channelLogo)
        val name: TextView = itemView.findViewById(R.id.channelName)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)

        fun bind(channel: IPTVChannel) {
            name.text = channel.name
            
            // Charger le logo avec Glide
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
            
            // Click sur la carte
            itemView.setOnClickListener {
                onChannelClick(channel)
            }
            
            // Click sur le favori
            favoriteIcon.setOnClickListener {
                onFavoriteClick(channel)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel_card, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channels[position])
    }

    override fun getItemCount() = channels.size
}
