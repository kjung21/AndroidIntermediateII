package com.kryptopass.devbytes.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.kryptopass.devbytes.database.getDatabase
import com.kryptopass.devbytes.repository.VideosRepository
import kotlinx.coroutines.launch

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way.
 * This allows data to survive configuration changes such as screen rotations.
 * In addition, background work such as fetching network results can continue through configuration changes
 * and deliver results after the new Fragment or Activity is available.
 * @param application The application that this viewmodel is attached to,
 * it's safe to hold a reference to applications across rotation
 * since Application is never recreated during activity or fragment lifecycle events.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val videosRepository = VideosRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    val playlist = videosRepository.videos

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
