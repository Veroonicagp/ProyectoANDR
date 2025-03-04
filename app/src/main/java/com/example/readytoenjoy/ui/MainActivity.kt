package com.example.readytoenjoy.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.R
import com.example.readytoenjoy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.zip.Inflater
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navegationBar = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_container) as NavHostFragment
        val navController = navHostFragment.navController
        navegationBar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{
            _,_,args->

            binding.bottomNavigation.isVisible = args?.getBoolean("showNavbar",true) ?: true
        }




    }
}