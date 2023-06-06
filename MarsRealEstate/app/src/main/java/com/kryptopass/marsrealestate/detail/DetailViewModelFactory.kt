package com.kryptopass.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kryptopass.marsrealestate.network.MarsProperty
import timber.log.Timber

/**
 * Simple ViewModel factory that provides the MarsProperty and context to the ViewModel.
 */
class DetailViewModelFactory(
        private val marsProperty: MarsProperty,
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(marsProperty, application) as T
        }
        Timber.e("Unknown ViewModel class")
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
