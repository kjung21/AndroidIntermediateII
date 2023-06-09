package com.kryptopass.sleepdawgs.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kryptopass.sleepdawgs.R
import com.kryptopass.sleepdawgs.database.SleepDatabase
import com.kryptopass.sleepdawgs.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep,
 * which are saved in a database.
 * Cumulative data is displayed in a simple scrollable TextView.
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when Fragment is ready to display content to screen.
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // get reference to binding object and inflate fragment views
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val viewModel =
            ViewModelProvider(
                this, viewModelFactory)[SleepTrackerViewModel::class.java]
        with (binding) {
            sleepTrackerViewModel = viewModel
            lifecycleOwner = this@SleepTrackerFragment
        }

        // add an Observer on state variable for showing a Snackbar message when CLEAR button is pressed
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()
                // reset state to make sure snackbar is only shown once,
                // even if the device has a configuration change
                viewModel.doneShowingSnackbar()
            }
        })

        // add an Observer on state variable for Navigating when STOP button is pressed
        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner) { night ->
            night?.let {
                // need to get navController from this, because button is not ready,
                // and it just has to be a view
                // for some reason, this only matters if we hit stop again after using back button,
                // not if we hit stop and choose a quality
                // also, in Navigation Editor, for Quality -> Tracker,
                // check "Inclusive" for popping the stack to get the correct behavior
                // if we press stop multiple times followed by back
                // also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )
                // reset state to make sure we only navigate once,
                // even if the device has a configuration change
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner) { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(night)
                )
                viewModel.onSleepDataQualityNavigated()
            }
        }

        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            viewModel.onSleepNightClicked(nightId)
        })

        binding.sleepList.adapter = adapter

        viewModel.nights.observe(viewLifecycleOwner) {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        }

        return binding.root
    }
}