package com.kryptopass.sleepdawgs.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.kryptopass.sleepdawgs.database.SleepDatabaseDao
import com.kryptopass.sleepdawgs.database.SleepNight
import com.kryptopass.sleepdawgs.formatNights
import kotlinx.coroutines.*
import androidx.lifecycle.viewModelScope

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var tonight = MutableLiveData<SleepNight?>()
    val nights = database.getAllNights()

    /**
     * Converted nights to Spanned for displaying.
     */
    val nightsString  = nights.map { nights ->
        formatNights(nights, application.resources)
    }

    /**
     * If tonight has not been set, then the START button should be visible.
     */
    val startButtonVisible = tonight.map {
        null == it
    }

    /**
     * If tonight has been set, then the STOP button should be visible.
     */
    val stopButtonVisible = tonight.map {
        null != it
    }

    /**
     * If there are any nights in the database, show the CLEAR button.
     */
    val clearButtonVisible = nights.map {
        it.isNotEmpty()
    }

    /**
     * Request a toast by setting this value to true.
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    /**
     * Variable that tells the Fragment to navigate to a specific [SleepQualityFragment]
     * This is private because we don't want to expose setting this value to the Fragment.
     */

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    /**
     * Call this immediately after calling `show()` on a toast.
     * It will clear the toast request,
     * so if the user rotates their phone it won't show a duplicate toast.
     */

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    /**
     * If this is non-null, immediately navigate to [SleepQualityFragment] and call [doneNavigating]
     */
    val navigateToSleepQuality: MutableLiveData<SleepNight?>
        get() = _navigateToSleepQuality

    /**
     * Call this immediately after navigating to [SleepQualityFragment]
     * It will clear the navigation request,
     * so if the user rotates their phone it won't navigate twice.
     */
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private val _navigateToSleepDataQuality = MutableLiveData<Long?>()
    val navigateToSleepDataQuality
        get() = _navigateToSleepDataQuality

    fun onSleepNightClicked(id: Long) {
        _navigateToSleepDataQuality.value = id
    }

    fun onSleepDataQualityNavigated() {
        _navigateToSleepDataQuality.value = null
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     * Handling the case of the stopped app or forgotten recording,
     * the start and end times will be the same.j
     * If the start time and end time are not the same,
     * then we do not have an unfinished recording.
     */
    private suspend fun getTonightFromDatabase(): SleepNight? {
        //return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            return night
        //}
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStartTracking() {
        viewModelScope.launch {
            // create new night, which captures current time, and insert it into database
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStopTracking() {
        viewModelScope.launch {
            // the return@label syntax is used for specifying
            // which function among several nested ones this statement returns from
            // in this case, we are specifying to return from launch(), not the lambda
            val oldNight = tonight.value ?: return@launch

            // update night in database to add end time
            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)

            // set state to navigate to SleepQualityFragment
            _navigateToSleepQuality.value = oldNight
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
        viewModelScope.launch {
            clear()

            // clear tonight since it is no longer in database
            tonight.value = null
        }

        // show a snackbar message, because it is friendly
        _showSnackbarEvent.value = true
    }
}