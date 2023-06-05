package com.kryptopass.gdg.add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import com.kryptopass.gdg.R
import com.kryptopass.gdg.databinding.AddGdgFragmentBinding
import com.google.android.material.snackbar.Snackbar

class AddGdgFragment : Fragment(), MenuProvider {

    private val viewModel: AddGdgViewModel by lazy {
        ViewModelProvider(this)[AddGdgViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = AddGdgFragmentBinding.inflate(inflater)

        with(binding) {
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            lifecycleOwner = this@AddGdgFragment
            addGdgViewModel = viewModel
        }

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.application_submitted),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.doneShowingSnackbar()

                binding.button.contentDescription = getString(R.string.submitted)
                binding.button.text = getString(R.string.done)
            }
        }

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        TODO("Not yet implemented")
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
