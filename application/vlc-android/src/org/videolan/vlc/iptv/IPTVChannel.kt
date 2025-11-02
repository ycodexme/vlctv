package org.videolan.vlc.iptv

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Modèle de données pour une chaîne IPTV
 */
@Parcelize
data class IPTVChannel(
    val name: String,
    val logo: String,
    val sourceUrl: String,
    var isFavorite: Boolean = false
) : Parcelable

/**
 * Modèle de données pour une catégorie de chaînes
 */
@Parcelize
data class IPTVCategory(
    val name: String,
    val channels: List<IPTVChannel>
) : Parcelable
