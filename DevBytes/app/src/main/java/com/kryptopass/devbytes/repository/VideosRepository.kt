package com.kryptopass.devbytes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kryptopass.devbytes.database.VideosDatabase
import com.kryptopass.devbytes.database.asDomainModel
import com.kryptopass.devbytes.domain.Video
import com.kryptopass.devbytes.network.Network
import com.kryptopass.devbytes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class VideosRepository(private val database: VideosDatabase) {

    /**
     * A playlist of videos that can be shown on the screen.
     */
    val videos: LiveData<List<Video>> = database.videoDao.getVideos().map {
        it.asDomainModel()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure
     * the database insert database operation happens on the IO dispatcher.
     * By switching to the IO dispatcher using `withContext`
     * this function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh videos is called")
            val playlist = Network.devbytes.getPlaylistAsync().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}
