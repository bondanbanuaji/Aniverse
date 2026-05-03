package com.example.aniverse

import android.os.Bundle
import android.view.View
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

        // Setup Toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        // Inisialisasi NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // Inisialisasi appBarConfiguration
        // Kita tidak menyertakan drawerLayout di sini agar NavigationUI tidak mencoba handle drawer secara otomatis (yang defaultnya LEFT)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.searchFragment, R.id.favoriteFragment, R.id.aboutFragment)
        )

        // Sync Toolbar dengan NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Handle Sidebar Selection secara manual untuk mencegah crash gravitasi LEFT/END
        binding.navView.setNavigationItemSelectedListener { item ->
            val handled = when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.aboutFragment -> {
                    navController.navigate(R.id.aboutFragment)
                    true
                }
                else -> false
            }
            if (handled) {
                binding.drawerLayout.closeDrawer(androidx.core.view.GravityCompat.END)
            }
            handled
        }

        // Custom Toolbar Click Listeners
        setupToolbarActions()
    }

    private fun setupToolbarActions() {
        binding.ivLanguage.setOnClickListener {
            showLanguageDialog()
        }

        binding.ivSearchIcon.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.ivMenuIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(androidx.core.view.GravityCompat.END)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(androidx.core.view.GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Indonesia")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_language))
            .setItems(languages) { _, which ->
                val localeCode = if (which == 0) "en" else "in"
                setLocale(localeCode)
            }
            .show()
    }

    private fun setLocale(langCode: String) {
        val locale = java.util.Locale(langCode)
        java.util.Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate() // Restart activity to apply changes
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}