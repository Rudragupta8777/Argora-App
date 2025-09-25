package com.argora.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.argora.app.databinding.ActivityMainBinding
import com.argora.app.main.AddHoldingBottomSheet
import com.argora.app.main.HoldingsAdapter
import com.argora.app.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory(application)
    }
    private lateinit var holdingsAdapter: HoldingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.fetchPortfolio()
    }

    private fun setupRecyclerView() {
        holdingsAdapter = HoldingsAdapter(mutableListOf())
        binding.holdingsRecyclerView.apply {
            adapter = holdingsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupListeners() {
        binding.fabAddHolding.setOnClickListener {
            AddHoldingBottomSheet().show(supportFragmentManager, "AddHoldingSheet")
        }

        binding.btnContinue.setOnClickListener {
            if (holdingsAdapter.itemCount > 0) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please add at least one holding to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.holdings.observe(this) { holdings ->
            holdingsAdapter.updateData(holdings)
            binding.btnContinue.isEnabled = holdings.isNotEmpty()

            // Update toolbar subtitle with portfolio summary
            val totalValue = holdings.sumOf { it.currentPrice * it.quantity }
            supportActionBar?.subtitle = "Total: ₹${"%.2f".format(totalValue)}"
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.holdingsRecyclerView.isVisible = !isLoading
            binding.btnContinue.isVisible = !isLoading
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    // Menu inflation
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Menu item selection handling
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.fetchPortfolio()
                Toast.makeText(this, "Refreshing portfolio...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                // Navigate to settings activity when implemented
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}