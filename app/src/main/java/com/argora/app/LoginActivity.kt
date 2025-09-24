package com.argora.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.argora.app.auth.AuthViewModel
import com.argora.app.data.SessionManager
import com.argora.app.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModel.AuthViewModelFactory(application)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupGoogleSignIn()
        setupObservers()

        binding.btnGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupObservers() {
        authViewModel.loginState.observe(this) { state ->
            when (state) {
                is AuthViewModel.LoginState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.btnGoogle.isEnabled = false
                }
                is AuthViewModel.LoginState.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    SessionManager.apiToken = state.response.token
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthViewModel.LoginState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.btnGoogle.isEnabled = true
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.w("LoginActivity", "Google sign in failed", e)
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // --- CHANGE 4: Add this new function from your old project ---
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
                        val firebaseIdToken = result.token
                        if (firebaseIdToken != null) {
                            Log.d("LoginActivity", "Got Firebase ID Token, sending to backend.")
                            authViewModel.loginWithBackend(firebaseIdToken)
                        } else {
                            Toast.makeText(this, "Failed to get Firebase ID Token.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Firebase sign-in failed
                    Toast.makeText(this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}