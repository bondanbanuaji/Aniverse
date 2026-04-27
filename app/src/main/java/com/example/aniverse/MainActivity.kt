package com.example.aniverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aniverse.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // AppBarConfiguration — daftarkan top-level destinations
        // (materi latnavdrawer + latnavigation)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.favoriteFragment,
                R.id.aboutFragment
            ),
            binding.drawerLayout
        )

        // Hubungkan Toolbar dengan NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Hubungkan BottomNav dengan NavController (materi latbottomnavigation)
        binding.bottomNav.setupWithNavController(navController)

        // Hubungkan NavDrawer dengan NavController (materi latnavdrawer)
        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}