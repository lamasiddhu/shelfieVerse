package com.example.shelfieverse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Note: Add this dependency to build.gradle.kts if not already present:
// implementation("io.coil-kt:coil-compose:2.4.0")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onBackClick: () -> Unit = {},
    onAddBookClick: (BookData) -> Unit = {},
    isLoading: Boolean = false
) {
    var bookName by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var ageRating by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Form validation
    val isFormValid = bookName.isNotBlank() &&
            description.isNotBlank() &&
            releaseYear.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add To Read Book",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8BC34A)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Upload Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            // TODO: Implement image picker when we have full functionality
                        }
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Book cover",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = "Add photo",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Tap to add book cover",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Book Name Field
            OutlinedTextField(
                value = bookName,
                onValueChange = { bookName = it },
                label = { Text("Enter Book Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                isError = bookName.isBlank()
            )

            // Rating and Release Year Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = rating,
                    onValueChange = { newValue ->
                        // Allow only numbers and decimal point, max 4.0
                        if (newValue.isEmpty() || newValue.matches(Regex("^([0-4]?)(\\.[0-9]?)?$"))) {
                            rating = newValue
                        }
                    },
                    label = { Text("Rating (0.0 - 4.0)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    placeholder = { Text("e.g., 3.5") }
                )

                OutlinedTextField(
                    value = releaseYear,
                    onValueChange = { newValue ->
                        // Allow only 4-digit years
                        if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                            releaseYear = newValue
                        }
                    },
                    label = { Text("Release Year") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    placeholder = { Text("e.g., 2023") },
                    isError = releaseYear.isNotBlank() && releaseYear.length != 4
                )
            }

            // Age Rating Field
            OutlinedTextField(
                value = ageRating,
                onValueChange = { ageRating = it },
                label = { Text("Age Rating (e.g., PG-13, R, G)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text("e.g., PG-13") }
            )

            // Genres Field
            OutlinedTextField(
                value = genres,
                onValueChange = { genres = it },
                label = { Text("Genres (Comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text("e.g., Action, Drama, Sci-Fi") },
                supportingText = { Text("Separate multiple genres with commas") }
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Enter Book Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 5,
                isError = description.isBlank(),
                supportingText = {
                    if (description.isBlank()) {
                        Text(
                            text = "Description is required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Book Button
            Button(
                onClick = {
                    if (isFormValid && !isLoading) {
                        val bookData = BookData(
                            name = bookName.trim(),
                            rating = rating.toFloatOrNull() ?: 0.0f,
                            releaseYear = releaseYear.trim(),
                            ageRating = ageRating.trim(),
                            genres = genres.trim(),
                            description = description.trim(),
                            imageUrl = imageUrl.trim()
                        )
                        onAddBookClick(bookData)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid && !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text("Adding Book...")
                    }
                } else {
                    Text(
                        text = "ADD BOOK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Add some space at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Data class for book information
data class BookData(
    val name: String,
    val rating: Float,
    val releaseYear: String,
    val ageRating: String,
    val genres: String,
    val description: String,
    val imageUrl: String
)

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {
    AddBookScreen()
}