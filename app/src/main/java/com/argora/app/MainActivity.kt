package com.argora.app

import android.os.Bundle
import android.widget.Toast
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
    }

    private fun setupObservers() {
        viewModel.holdings.observe(this) { holdings ->
            holdingsAdapter.updateData(holdings)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }
}