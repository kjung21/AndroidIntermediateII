package com.kryptopass.sleepdawgs.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kryptopass.sleepdawgs.database.SleepDatabaseDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * ViewModel for SleepQualityFragment.
 * @param sleepNightKey The key of the current night we are working on.
 */
class SleepQualityViewModel(
        private val sleepNightKey: Long = 0L,
        val database: SleepDatabaseDao) : ViewModel() {

    /**
     * Variable that tells the fragment whether it should navigate to [SleepTrackerFragment].
     * This is `private` because we don't want to expose ability to set [MutableLiveData] to [Fragment]
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    /**
     * Sets the sleep quality and updates the database.
     * Then navigates back to the SleepTrackerFragment.
     */
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            // IO is a thread pool for running operations that access disk,
            // such as our Room database
            //withContext(Dispatchers.IO) {
                val tonight = database.get(sleepNightKey) ?: return@launch
                tonight.sleepQuality = quality
                database.update(tonight)
            //}

            // setting this state variable to true will alert observer and trigger navigation
            _navigateToSleepTracker.value = true
        }
    }
}