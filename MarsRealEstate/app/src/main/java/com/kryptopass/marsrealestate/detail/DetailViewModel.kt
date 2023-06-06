package com.kryptopass.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.kryptopass.marsrealestate.R
import com.kryptopass.marsrealestate.network.MarsProperty
import timber.log.Timber

/**
 * The [ViewModel] associated with the [DetailFragment],
 * containing information about the selected [MarsProperty].
 */
class DetailViewModel(marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<MarsProperty>()
    val selectedProperty: LiveData<MarsProperty>
        get() = _selectedProperty

    init {
        Timber.i("init called!")
        _selectedProperty.value = marsProperty
    }

    // displayPropertyPrice formatted Transformation Map LiveData,
    // which displays the sale or rental price
    val displayPropertyPrice = selectedProperty.map {
        app.applicationContext.getString(
            when (it.isRental) {
                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price)
    }

    // displayPropertyType formatted Transformation Map LiveData,
    // which displays the "For Rent/Sale" String
    val displayPropertyType = selectedProperty.map {
        app.applicationContext.getString(R.string.display_type,
            app.applicationContext.getString(
                when(it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }))
    }
}
