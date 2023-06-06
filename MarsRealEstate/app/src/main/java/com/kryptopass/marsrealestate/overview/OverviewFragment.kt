package com.kryptopass.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kryptopass.marsrealestate.R
import com.kryptopass.marsrealestate.databinding.FragmentOverviewBinding
import com.kryptopass.marsrealestate.network.MarsApiFilter
import timber.log.Timber

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment(), MenuProvider {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this)[OverviewViewModel::class.java]
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        Timber.i("onCreateView called!")

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda
        // that tells the viewModel when our property is clicked
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe navigateToSelectedProperty LiveData and Navigate when it is not null
        // After navigating, call displayPropertyDetailsComplete()
        // so that the ViewModel is ready for another navigation event.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                // let ViewModel know we have made navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        }

        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.overflow_menu, menu)
    }

    /**
     * Updates filter in [OverviewViewModel] when menu items are selected from overflow menu.
     */
    override fun onMenuItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu ->  MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }
}
