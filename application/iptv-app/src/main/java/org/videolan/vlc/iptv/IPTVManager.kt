package org.videolan.vlc.iptv

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Gestionnaire des chaînes IPTV
 */
class IPTVManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("iptv_prefs", Context.MODE_PRIVATE)
    private val FAVORITES_KEY = "favorites"
    private val API_URL = "https://channels.vdfr.uk/channels"
    
    /**
     * Charge toutes les catégories de chaînes depuis l'API
     */
    suspend fun loadCategories(): List<IPTVCategory> = withContext(Dispatchers.IO) {
        try {
            val json = fetchChannelsData()
            parseCategories(json)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Récupère les données JSON depuis l'API
     */
    private fun fetchChannelsData(): String {
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("accept", "application/json")
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?
        
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        
        reader.close()
        connection.disconnect()
        
        return response.toString()
    }
    
    /**
     * Parse le JSON et crée les catégories
     */
    private fun parseCategories(jsonString: String): List<IPTVCategory> {
        val categories = mutableListOf<IPTVCategory>()
        val favorites = loadFavorites()
        
        try {
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            
            while (keys.hasNext()) {
                val categoryName = keys.next()
                val channelsArray = jsonObject.getJSONArray(categoryName)
                val channels = mutableListOf<IPTVChannel>()
                
                for (i in 0 until channelsArray.length()) {
                    val channelObj = channelsArray.getJSONObject(i)
                    val sourceUrl = channelObj.getString("source_url")
                    
                    val channel = IPTVChannel(
                        name = channelObj.getString("name"),
                        logo = channelObj.getString("logo"),
                        sourceUrl = sourceUrl,
                        isFavorite = favorites.contains(sourceUrl)
                    )
                    channels.add(channel)
                }
                
                if (channels.isNotEmpty()) {
                    categories.add(IPTVCategory(categoryName, channels))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return categories
    }
    
    /**
     * Charge les favoris depuis SharedPreferences
     */
    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }
    
    /**
     * Ajoute/retire une chaîne des favoris
     */
    fun toggleFavorite(channel: IPTVChannel) {
        val favorites = loadFavorites().toMutableSet()
        
        if (channel.isFavorite) {
            favorites.remove(channel.sourceUrl)
        } else {
            favorites.add(channel.sourceUrl)
        }
        
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
        channel.isFavorite = !channel.isFavorite
    }
    
    /**
     * Récupère toutes les chaînes favorites
     */
    suspend fun getFavoriteChannels(): List<IPTVChannel> = withContext(Dispatchers.IO) {
        val allCategories = loadCategories()
        allCategories.flatMap { it.channels }.filter { it.isFavorite }
    }
    
    /**
     * Recherche des chaînes par nom
     */
    suspend fun searchChannels(query: String): List<IPTVChannel> = withContext(Dispatchers.IO) {
        val allCategories = loadCategories()
        allCategories.flatMap { it.channels }
            .filter { it.name.contains(query, ignoreCase = true) }
    }
}
