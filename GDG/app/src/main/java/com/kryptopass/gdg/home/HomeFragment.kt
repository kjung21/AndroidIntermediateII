package com.kryptopass.gdg.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kryptopass.gdg.R
import com.kryptopass.gdg.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel

        viewModel.navigateToSearch.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_homeFragment_to_gdgListFragment)
                viewModel.onNavigatedToSearch()
            }
        }
        return binding.root
    }
}
