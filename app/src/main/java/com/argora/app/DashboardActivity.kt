package com.argora.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.argora.app.Fragment.AssistantFragment
import com.argora.app.Fragment.ProfileFragment
import com.argora.app.Fragment.WeatherFragment
import com.argora.app.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the default fragment
        if (savedInstanceState == null) {
            replaceFragment(WeatherFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_weather -> WeatherFragment()
                R.id.nav_assistant -> AssistantFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> WeatherFragment() // Default case
            }
            replaceFragment(selectedFragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}