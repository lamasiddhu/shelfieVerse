package com.example.shelfieverse.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.shelfieverse.ViewModel.BookViewModel
import com.example.shelfieverse.model.BookModel
import com.example.shelfieverse.repository.BookRepositoryImp
import com.example.shelfieverse.ui.screen.BookData
import com.example.shelfieverse.ui.screen.UpdateBookScreen
import com.example.shelfieverse.ui.theme.BooktokTheme

class UpdateBookActivity : ComponentActivity() {
    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel
        val repo = BookRepositoryImp()
        bookViewModel = BookViewModel(repo)

        // Get the book data from intent (if passed)
        val bookId = intent.getStringExtra("bookId") ?: ""
        val bookToEdit = intent.getParcelableExtra<BookModel>("bookToEdit")

        // Configure window for edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BooktokTheme {
                UpdateBookScreenContainer(bookToEdit, bookId)
            }
        }
    }

    @Composable
    private fun UpdateBookScreenContainer(bookModel: BookModel?, bookId: String) {
        var isLoading by remember { mutableStateOf(false) }

        // Convert BookModel to BookData or use default
        val bookData = remember {
            if (bookModel != null) {
                BookData(
                    name = bookModel.BookName ?: "",
                    rating = bookModel.Rating ?: 4.0f, // Use actual rating from model
                    releaseYear = bookModel.releaseYear ?: "",
                    ageRating = bookModel.ageRating ?: "",
                    genres = bookModel.genres?.joinToString(", ") ?: "Fiction", // Convert list to string
                    description = bookModel.description ?: "",
                    imageUrl = bookModel.imageUrl ?: ""
                )
            } else {
                // Default book data if none provided
                BookData(
                    name = "Sample Book",
                    rating = 4.0f,
                    releaseYear = "2023",
                    ageRating = "PG-13",
                    genres = "Fiction",
                    description = "This is a sample book to demonstrate the update functionality.",
                    imageUrl = ""
                )
            }
        }

        UpdateBookScreen(
            book = bookData,
            onBackClick = {
                finish()
            },
            onUpdateBookClick = { updatedBookData ->
                handleUpdateBook(updatedBookData, bookId) { loading ->
                    isLoading = loading
                }
            },
            onDeleteBookClick = {
                handleDeleteBook(bookId) { loading ->
                    isLoading = loading
                }
            },
            isLoading = isLoading
        )
    }

    private fun handleUpdateBook(bookData: BookData, bookId: String, onLoadingChange: (Boolean) -> Unit) {
        // Validate required fields
        if (bookData.name.isBlank() || bookData.description.isBlank()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate bookId
        if (bookId.isBlank()) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            return
        }

        onLoadingChange(true)

        // Convert BookData to Map for the update
        val updateData = mutableMapOf<String, Any>(
            "BookName" to bookData.name,
            "description" to bookData.description,
            "ageRating" to bookData.ageRating,
            "releaseYear" to bookData.releaseYear,
            "imageUrl" to bookData.imageUrl,
            "Rating" to bookData.rating,
            "genres" to bookData.genres.split(",").map { it.trim() }
        )

        // Debug log to verify data
        android.util.Log.d("UpdateBook", "Updating book with ID: $bookId")
        android.util.Log.d("UpdateBook", "Update data: $updateData")

        // Call the ViewModel to update the book in Firebase
        bookViewModel.updateBook(bookId, updateData) { success, message ->
            onLoadingChange(false)

            android.util.Log.d("UpdateBook", "Update result - Success: $success, Message: $message")

            if (success) {
                Toast.makeText(this@UpdateBookActivity, "Book updated successfully!", Toast.LENGTH_LONG).show()
                finish() // Go back to previous screen
            } else {
                Toast.makeText(this@UpdateBookActivity, "Failed to update: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleDeleteBook(bookId: String, onLoadingChange: (Boolean) -> Unit) {
        // Validate bookId
        if (bookId.isBlank()) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            return
        }

        onLoadingChange(true)

        // Debug log
        android.util.Log.d("DeleteBook", "Deleting book with ID: $bookId")

        // Call the ViewModel to delete the book from Firebase
        bookViewModel.deleteBook(bookId) { success, message ->
            onLoadingChange(false)

            android.util.Log.d("DeleteBook", "Delete result - Success: $success, Message: $message")

            if (success) {
                Toast.makeText(this@UpdateBookActivity, "Book deleted successfully!", Toast.LENGTH_LONG).show()
                finish() // Go back to previous screen
            } else {
                Toast.makeText(this@UpdateBookActivity, "Failed to delete: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
}