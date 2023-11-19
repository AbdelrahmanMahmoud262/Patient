package com.androdevelopment.patient.views.activities

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.androdevelopment.patient.R
import com.androdevelopment.patient.databinding.ActivityMainBinding
import com.androdevelopment.patient.util.ConnectivityObserver
import com.androdevelopment.patient.views.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpBottomNavigation()
        setConnectionStatus()
    }

    private fun initStatus(isVisible: Boolean) {
        if (isVisible) {
            scaleView(binding.containerStatus, 0f, 1f)
            binding.containerStatus.visibility = View.VISIBLE
        } else {
            scaleView(binding.containerStatus, 1f, 0f)
            binding.containerStatus.visibility = View.GONE
        }
    }

    fun visibilityBottomNav(visible:Boolean){
        if (visible) scaleView(binding.bottomNavigationView,0f,1f)
        else scaleView(binding.bottomNavigationView,1f,0f)
    }

    private fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim = ScaleAnimation(
            1f, 1f, // Start and end values for the X axis scaling
            startScale, endScale, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f // Pivot point of Y scaling
        )
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 1000
        v.startAnimation(anim)
    }

    private fun setConnectionStatus() {
        lifecycleScope.launch {
            NetworkConnectivityObserver(this@MainActivity).observe().collect {
                when (it) {
                    ConnectivityObserver.Status.InternetAvailable -> {
                        initStatus(false)
                    }

                    ConnectivityObserver.Status.InternetUnavailable -> {
                        initStatus(true)
                    }

                    ConnectivityObserver.Status.InternetLost -> {
                        initStatus(true)
                    }

                    else -> {
                        initStatus(true)
                    }
                }
            }
        }
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigationView, navController)
    }

}