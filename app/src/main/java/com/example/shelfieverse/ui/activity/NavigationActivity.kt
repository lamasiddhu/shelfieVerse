package com.example.shelfieverse.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.shelfieverse.model.BookModel
import com.example.shelfieverse.ui.screen.BookDashboardScreen
import com.example.shelfieverse.ui.theme.BooktokTheme

class NavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BooktokTheme {
                MainNavigationScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    @Composable
    private fun MainNavigationScreen() {
        val context = LocalContext.current
        var selectedTab by remember { mutableStateOf(0) }
        var dashboardRefreshTrigger by remember { mutableStateOf(0) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            dashboardRefreshTrigger++
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    0 -> {
                        key(dashboardRefreshTrigger) {
                            BookDashboardScreen(
                                onAddBookClick = {
                                    val intent = Intent(context, AddBookActivity::class.java)
                                    context.startActivity(intent)
                                },
                                onBookClick = { book ->
                                    val intent = Intent(context, BookDetailActivity::class.java)
                                    val bookModel = BookModel(
                                        BookId = book.id,
                                        BookName = book.title,
                                        description = book.description,
                                        ageRating = "PG-13",
                                        releaseYear = book.publicationYear,
                                        imageUrl = "",
                                        Rating = 4.0f,
                                        genres = if (book.genre.isNotEmpty()) listOf(book.genre) else emptyList()
                                    )
                                    intent.putExtra("MOVIE_KEY", bookModel)
                                    context.startActivity(intent)
                                },
                                onEditBookClick = { book ->
                                    val intent = Intent(context, UpdateBookActivity::class.java)
                                    val bookModel = BookModel(
                                        BookId = book.id,
                                        BookName = book.title,
                                        description = book.description,
                                        ageRating = "PG-13",
                                        releaseYear = book.publicationYear,
                                        imageUrl = "",
                                        Rating = 4.0f,
                                        genres = if (book.genre.isNotEmpty()) listOf(book.genre) else emptyList()
                                    )
                                    intent.putExtra("bookToEdit", bookModel)
                                    intent.putExtra("bookId", book.id)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                    1 -> SettingsScreen()
                }
            }
        }
    }

    @Composable
    private fun SettingsScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "App Settings",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Settings options will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(this@NavigationActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Logout")
            }

            OutlinedButton(
                onClick = {
                    // TODO: Implement about functionality
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("About")
            }
        }
    }
}