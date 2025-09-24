package com.argora.app.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.argora.app.data.AddHoldingRequest
import com.argora.app.data.Holding
import com.argora.app.data.NewHolding
import com.argora.app.data.SessionManager
import com.argora.app.data.StockMatch
import com.argora.app.network.AlphaVantageRetrofitInstance
import com.argora.app.network.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // --- LiveData for UI State ---
    private val _holdings = MutableLiveData<List<Holding>>()
    val holdings: LiveData<List<Holding>> = _holdings

    private val _searchResults = MutableLiveData<List<StockMatch>>()
    val searchResults: LiveData<List<StockMatch>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var searchJob: Job? = null

    // This should be loaded from SharedPreferences after login
    private val authToken: String?
        get() = SessionManager.apiToken?.let { "Bearer $it" }

    fun fetchPortfolio() {
        viewModelScope.launch {
            if (authToken == null) {
                _error.value = "User is not authenticated."
                return@launch
            }
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getPortfolio(authToken!!)
                if (response.isSuccessful && response.body() != null) {
                    _holdings.value = response.body()?.portfolio?.holdings ?: emptyList()
                } else {
                    _error.value = "Failed to fetch portfolio"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchStock(keywords: String) {
        searchJob?.cancel() // Cancel previous job
        if (keywords.length < 2) return

        searchJob = viewModelScope.launch {
            delay(300) // Debounce to avoid too many API calls
            try {
                val response = AlphaVantageRetrofitInstance.api.searchSymbol(
                    AlphaVantageRetrofitInstance.createSearchQuery(keywords)
                )
                _searchResults.value = response.bestMatches
            } catch (e: Exception) {
                Log.e("MainViewModel", "Stock search failed", e)
            }
        }
    }

    fun addHolding(newHolding: NewHolding) {
        viewModelScope.launch {
            if (authToken == null) {
                _error.value = "User is not authenticated."
                return@launch
            }
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.addHolding(authToken!!, AddHoldingRequest(newHolding))
                if (response.isSuccessful) {
                    fetchPortfolio() // Refresh the list
                } else {
                    _error.value = "Failed to add holding"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ViewModel Factory
    class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}