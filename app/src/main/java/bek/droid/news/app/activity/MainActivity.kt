package bek.droid.news.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import bek.droid.news.R
import bek.droid.news.common.hide
import bek.droid.news.common.invisible
import bek.droid.news.common.show
import bek.droid.news.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
    }

    private fun initViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.newsVerticalFragment) {
                binding.bottomNavView.invisible()
            } else {
                binding.bottomNavView.show()
            }
        }

        setupNavigation(navController)
    }

    fun setSearchAsSelected() {
        binding.bottomNavView.selectedItemId = R.id.searchFragment
    }

    private fun setupNavigation(navController: NavController) {
        binding.bottomNavView.setupWithNavController(navController)
    }
}