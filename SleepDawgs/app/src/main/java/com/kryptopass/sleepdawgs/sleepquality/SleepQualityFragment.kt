package com.kryptopass.sleepdawgs.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kryptopass.sleepdawgs.R
import com.kryptopass.sleepdawgs.database.SleepDatabase
import com.kryptopass.sleepdawgs.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get reference to binding object and inflate fragment views
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_quality, container, false
        )

        val application = requireNotNull(this.activity).application

        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())

        // create an instance of the ViewModel Factory
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)

        // get reference to ViewModel associated with this fragment
        val sleepQualityViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[SleepQualityViewModel::class.java]

        binding.sleepQualityViewModel = sleepQualityViewModel

        // add an Observer to state variable for Navigating when a Quality icon is tapped
        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {

            if (it == true) {
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
                )
                // reset state to make sure we only navigate once,
                // even if the device has a configuration change
                sleepQualityViewModel.doneNavigating()
            }
        }

        return binding.root
    }
}