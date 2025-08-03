package com.example.shelfieverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.shelfieverse.model.UserModel
import com.example.shelfieverse.ViewModel.UserViewModel
import com.example.shelfieverse.repository.UserRepositoryImp
import com.example.shelfieverse.ui.screen.RegisterScreen
import com.example.shelfieverse.ui.theme.BooktokTheme

class RegisterActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel
        val userRepository = UserRepositoryImp()
        userViewModel = UserViewModel(userRepository)

        // Configure window for edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BooktokTheme {
                RegisterScreenContainer()
            }
        }
    }

    @Composable
    private fun RegisterScreenContainer() {
        var isLoading by remember { mutableStateOf(false) }

        RegisterScreen(
            onRegisterClick = { userName, email, phoneNumber, password, confirmPassword ->
                handleRegister(userName, email, phoneNumber, password, confirmPassword) { loading ->
                    isLoading = loading
                }
            },
            onBackClick = {
                navigateToLogin()
            },
            isLoading = isLoading
        )
    }

    private fun handleRegister(
        userName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        onLoadingChange: (Boolean) -> Unit
    ) {
        // Validate passwords match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        onLoadingChange(true)

        userViewModel.signup(email, password) { success, message, userId ->
            if (success) {
                val userModel = UserModel(
                    userId,
                    email,
                    userName,
                    phoneNumber
                )
                addUser(userModel, onLoadingChange)
            } else {
                onLoadingChange(false)
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser(userModel: UserModel, onLoadingChange: (Boolean) -> Unit) {
        // Log before starting the operation
        Log.d("RegisterActivity", "Starting to add user: ${userModel.userId}")

        userViewModel.addUserToDatabase(userModel.userId, userModel) { success, message ->
            onLoadingChange(false)

            // Log the result of the operation
            if (success) {
                Log.d("RegisterActivity", "User added successfully: $message")
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                // For errors, use Log.e (error)
                Log.e("RegisterActivity", "Failed to add user: $message")
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
            }

            // Log when the loading is dismissed
            Log.d("RegisterActivity", "Loading dismissed")
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}