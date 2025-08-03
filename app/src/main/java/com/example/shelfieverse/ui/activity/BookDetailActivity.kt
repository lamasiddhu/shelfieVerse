package com.example.shelfieverse.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.shelfieverse.model.BookModel
import com.example.shelfieverse.ui.screen.BookDetail
import com.example.shelfieverse.ui.screen.BookDetailScreen
import com.example.shelfieverse.ui.theme.BooktokTheme

class BookDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure window for edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Get the BookModel from the Intent
        val bookModel = intent.getParcelableExtra<BookModel>("MOVIE_KEY")

        if (bookModel == null) {
            Toast.makeText(this, "Book data is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            BooktokTheme {
                BookDetailScreenContainer(bookModel)
            }
        }
    }

    @Composable
    private fun BookDetailScreenContainer(bookModel: BookModel) {
        // Convert BookModel to BookDetail for the Compose screen
        val bookDetail = BookDetail(
            id = bookModel.BookId ?: "",
            name = bookModel.BookName ?: "Unknown Book",
            rating = 4.5f, // Default rating since we're not sure of BookModel.Rating type
            releaseYear = bookModel.releaseYear ?: "",
            ageRating = bookModel.ageRating ?: "",
            description = bookModel.description ?: "No description available",
            imageUrl = bookModel.imageUrl ?: "",
            genres = listOf("Fiction", "Adventure"), // Default genres
            moral = "Every book teaches us valuable lessons about life, relationships, and personal growth."
        )

        BookDetailScreen(
            book = bookDetail,
            onBackClick = {
                finish()
            },
            onReadBookClick = {
                Toast.makeText(
                    this@BookDetailActivity,
                    "Opening ${bookDetail.name} for reading...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}