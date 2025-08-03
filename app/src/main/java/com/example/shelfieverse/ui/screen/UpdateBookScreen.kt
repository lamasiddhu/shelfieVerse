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
import androidx.compose.material.icons.filled.Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookScreen(
    book: BookData, // Existing book data to edit
    onBackClick: () -> Unit = {},
    onUpdateBookClick: (BookData) -> Unit = {},
    onDeleteBookClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    var bookName by remember { mutableStateOf(book.name) }
    var rating by remember { mutableStateOf(book.rating.toString()) }
    var releaseYear by remember { mutableStateOf(book.releaseYear) }
    var ageRating by remember { mutableStateOf(book.ageRating) }
    var genres by remember { mutableStateOf(book.genres) }
    var description by remember { mutableStateOf(book.description) }
    var imageUrl by remember { mutableStateOf(book.imageUrl) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Form validation
    val isFormValid = bookName.isNotBlank() &&
            description.isNotBlank() &&
            releaseYear.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Update Book",
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
                actions = {
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Book",
                            tint = MaterialTheme.colorScheme.error
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
                                text = "Tap to update book cover",
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
                label = { Text("Book Name") },
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
                        // Allow only numbers and decimal point, max 10.0
                        if (newValue.isEmpty() || newValue.matches(Regex("^([0-9]?)(\\.[0-9]?)?$|^10(\\.0)?$"))) {
                            rating = newValue
                        }
                    },
                    label = { Text("Rating (0.0 - 10.0)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    placeholder = { Text("e.g., 8.5") }
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
                label = { Text("Book Description") },
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

            // Update Book Button
            Button(
                onClick = {
                    if (isFormValid && !isLoading) {
                        val updatedBook = BookData(
                            name = bookName.trim(),
                            rating = rating.toFloatOrNull() ?: 0.0f,
                            releaseYear = releaseYear.trim(),
                            ageRating = ageRating.trim(),
                            genres = genres.trim(),
                            description = description.trim(),
                            imageUrl = imageUrl.trim()
                        )
                        onUpdateBookClick(updatedBook)
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
                        Text("Updating Book...")
                    }
                } else {
                    Text(
                        text = "UPDATE BOOK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Delete Book Button
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "DELETE BOOK",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Add some space at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Book") },
            text = {
                Text("Are you sure you want to delete \"$bookName\"? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteBookClick()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateBookScreenPreview() {
    val sampleBook = BookData(
        name = "Sample Book",
        rating = 8.5f,
        releaseYear = "2023",
        ageRating = "PG-13",
        genres = "Action, Drama",
        description = "This is a sample book description for preview purposes.",
        imageUrl = ""
    )
    UpdateBookScreen(book = sampleBook)
}