package com.kryptopass.gdg

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {

        return try {

            Result.success()
        } catch (e: Exception) {
            Timber.e("Work request for sync is interrupted: ${e.message}")
            Result.retry()
        }
    }
}