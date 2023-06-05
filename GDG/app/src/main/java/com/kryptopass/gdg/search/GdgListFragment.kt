package com.kryptopass.gdg.search

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kryptopass.gdg.databinding.FragmentGdgListBinding
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kryptopass.gdg.R

private const val LOCATION_PERMISSION_REQUEST = 1

private const val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

class GdgListFragment : Fragment(), MenuProvider {

    private val viewModel: GdgListViewModel by lazy {
        ViewModelProvider(this)[GdgListViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentGdgListBinding.inflate(inflater)

        with (binding) {
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            lifecycleOwner = this@GdgListFragment
            // Giving the binding access to the OverviewViewModel
            gdgListViewModel = viewModel
        }

        val adapter = GdgListAdapter(GdgClickListener { chapter ->
            val destination = Uri.parse(chapter.website)
            startActivity(Intent(Intent.ACTION_VIEW, destination))
        })

        // Sets the adapter of the RecyclerView
        binding.gdgChapterList.adapter = adapter

        viewModel.showNeedLocation.observe(viewLifecycleOwner
        ) { show -> // Snackbar is like Toast but it lets us show forever
            if (show) {
                Snackbar.make(
                    binding.root,
                    "No location. Enable location in settings (hint: test with Maps) then check app permissions!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.regionList.observe(viewLifecycleOwner, object: Observer<List<String>> {
            override fun onChanged(data: List<String>) {
                data ?: return
                val chipGroup = binding.regionList
                val inflator = LayoutInflater.from(chipGroup.context)

                val children = data.map { regionName ->
                    val chip = inflator.inflate(R.layout.region, chipGroup, false) as Chip
                    chip.text = regionName
                    chip.tag = regionName
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        viewModel.onFilterChanged(button.tag as String, isChecked)
                    }
                    chip
                }

                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip)
                }
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLastLocationOrStartLocationUpdates()
    }

    /**
     * Show the user a dialog asking for permission to use location.
     */
    private fun requestLocationPermission() {
        // https://write.agrevolution.in/deprecated-requestpermissions-131c39375916
        requestPermissions(arrayOf(LOCATION_PERMISSION), LOCATION_PERMISSION_REQUEST)
    }

    /**
     * Request the last location of this device, if known, otherwise start location updates.
     * The last location is cached from the last application to request location.
     */
    private fun requestLastLocationOrStartLocationUpdates() {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                startLocationUpdates(fusedLocationClient)
            } else {
                viewModel.onLocationUpdated(location)
            }
        }
    }

    /**
     * Start location updates, this will ask the operating system to figure out the devices location.
     */
    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }

        // val request = LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply {
            setMinUpdateDistanceMeters(500.0f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()
        val callback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult?.lastLocation ?: return
                viewModel.onLocationUpdated(location)
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }

    /**
     * This will be called by Android when the user responds to the permission request.
     * If granted, continue with the operation that the user gave us permission to do.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // https://write.agrevolution.in/deprecated-requestpermissions-131c39375916
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLastLocationOrStartLocationUpdates()
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        TODO("Not yet implemented")
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
