package com.kryptopass.sleepdawgs.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kryptopass.sleepdawgs.database.SleepDatabaseDao
import timber.log.Timber

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the key for the night and the SleepDatabaseDao to the ViewModel.
 */
class SleepQualityViewModelFactory(
        private val sleepNightKey: Long,
        private val dataSource: SleepDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)) {
            return SleepQualityViewModel(sleepNightKey, dataSource) as T
        }
        Timber.e("Unknown ViewModel class")
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}