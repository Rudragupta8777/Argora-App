package com.argora.app.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.argora.app.data.LoginRequest
import com.argora.app.data.LoginResponse
import com.argora.app.network.RetrofitInstance
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    sealed class LoginState {
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun loginWithBackend(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitInstance.api.login(LoginRequest(idToken))
                if (response.isSuccessful && response.body()?.success == true) {
                    _loginState.value = LoginState.Success(response.body()!!)
                    // Here you would save the API token for future use
                    // For example, in SharedPreferences.
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Authentication failed"
                    _loginState.value = LoginState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Network error: ${e.message}")
            }
        }
    }

    // Factory to create the ViewModel
    class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}