package com.kryptopass.gdg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kryptopass.gdg.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupNavigation()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    /**
     * Called when the hamburger menu or back button are pressed on the Toolbar
     * Delegate this to Navigation.
     */
    override fun onSupportNavigateUp() = navigateUp(navController, binding.drawerLayout)

    /**
     * Setup Navigation for this Activity
     */
    private fun setupNavigation() {

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, binding.drawerLayout)

        binding.navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
            when (destination.id) {
                R.id.home -> {
                    toolBar.setDisplayShowTitleEnabled(false)
                    binding.heroImage.visibility = View.VISIBLE
                }

                else -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                    binding.heroImage.visibility = View.GONE
                }
            }
        }
    }
}
