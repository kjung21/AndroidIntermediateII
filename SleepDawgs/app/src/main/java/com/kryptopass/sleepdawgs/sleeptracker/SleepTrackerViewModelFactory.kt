package com.kryptopass.sleepdawgs.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kryptopass.sleepdawgs.database.SleepDatabaseDao
import timber.log.Timber

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class SleepTrackerViewModelFactory(
        private val dataSource: SleepDatabaseDao,
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            return SleepTrackerViewModel(dataSource, application) as T
        }
        Timber.e("Unknown ViewModel class")
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}