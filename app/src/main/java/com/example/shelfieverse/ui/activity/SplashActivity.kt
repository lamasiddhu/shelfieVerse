package com.example.shelfieverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log // ✅ Added missing import
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelfieverse.ui.theme.BooktokTheme
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            BooktokTheme {
                SplashScreen()
            }
        }

        // Check auth state and sign in anonymously if needed
        signInAndNavigate()
    }

    private fun signInAndNavigate() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Already signed in (e.g., from previous session)
            navigateToMain()
        } else {
            // Try anonymous sign-in
            auth.signInAnonymously()
                .addOnSuccessListener { result ->
                    Log.d("Auth", "Signed in anonymously: ${result.user?.uid}")
                    navigateToMain()
                }
                .addOnFailureListener { exception ->
                    Log.e("Auth", "Anonymous sign-in failed: ${exception.message}", exception)
                    // Fallback: go to login (or show error)
                    navigateToLogin()
                }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    @Composable
    private fun SplashScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.size(120.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "📚", fontSize = 60.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "BookTok",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover Your Next Great Read",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Loading...",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
        }
    }
}