package com.kryptopass.sleepdawgs.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kryptopass.sleepdawgs.R
import com.kryptopass.sleepdawgs.database.SleepDatabase
import com.kryptopass.sleepdawgs.databinding.FragmentSleepDetailBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SleepDetailFragment.OnFragmentInteractionListener] interface to handle interaction events.
 * Use [SleepDetailFragment.newInstance] factory method to create an instance of this fragment.
 */
class SleepDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Timber.i("SleepDetailFragment onCreateView called")

        // get reference to binding object and inflate fragment views
        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())

        // create instance of ViewModel Factory
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)

        // get reference to ViewModel associated with this fragment
        val sleepDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory)[SleepDetailViewModel::class.java]

        binding.sleepDetailViewModel = sleepDetailViewModel

        binding.lifecycleOwner = this

        // add an Observer to state variable for Navigating when a Quality icon is tapped
        sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                        SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
                // reset state to make sure we only navigate once,
                // even if the device has a configuration change
                sleepDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
