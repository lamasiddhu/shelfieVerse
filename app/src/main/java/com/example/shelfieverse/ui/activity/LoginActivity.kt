package com.example.shelfieverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.shelfieverse.ViewModel.UserViewModel
import com.example.shelfieverse.repository.UserRepositoryImp
import com.example.shelfieverse.ui.screen.LoginScreen
import com.example.shelfieverse.ui.theme.BooktokTheme // You'll need to create this theme

class LoginActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel
        val repo = UserRepositoryImp()
        userViewModel = UserViewModel(repo)

        // Configure window for edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BooktokTheme {
                LoginScreenContainer()
            }
        }
    }

    @Composable
    private fun LoginScreenContainer() {
        var isLoading by remember { mutableStateOf(false) }

        LoginScreen(
            onLoginClick = { email, password ->
                handleLogin(email, password) { loading ->
                    isLoading = loading
                }
            },
            onSignUpClick = {
                navigateToRegister()
            },
            onForgetPasswordClick = {
                // TODO: Implement forgot password
                Toast.makeText(this@LoginActivity, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show()
            },
            isLoading = isLoading
        )
    }

    private fun handleLogin(email: String, password: String, onLoadingChange: (Boolean) -> Unit) {
        onLoadingChange(true)

        userViewModel.login(email, password) { success, message ->
            onLoadingChange(false)

            if (success) {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                navigateToNavigation()
            } else {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToNavigation() {
        val intent = Intent(this@LoginActivity, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}