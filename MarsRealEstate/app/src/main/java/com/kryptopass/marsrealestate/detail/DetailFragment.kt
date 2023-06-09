package com.kryptopass.marsrealestate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kryptopass.marsrealestate.databinding.FragmentDetailBinding
import timber.log.Timber

/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel],
 * which it gets as a Parcelable property through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Timber.i("onCreateView called!")

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val marsProperty = DetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = DetailViewModelFactory(marsProperty, application)

        binding.viewModel = ViewModelProvider(
            this, viewModelFactory)[DetailViewModel::class.java]

        return binding.root
    }
}
