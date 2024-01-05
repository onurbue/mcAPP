package com.example.projetocma

import account.AccountFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.projetocma.databinding.ActivityNavBarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavBar : AppCompatActivity() {
    private lateinit var binding: ActivityNavBarBinding
    private lateinit var navController: NavController

    val homeFragment = MuseusExplore()
    val qrCodeFragment = QrCode()
    val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragCont) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navController.navigate(R.id.museusExplore)
                R.id.qrcodenav -> navController.navigate(R.id.qrCode)
                R.id.profile -> navController.navigate(R.id.accountFragment)
                else -> {
                }
            }
            true
        }
    }

}
