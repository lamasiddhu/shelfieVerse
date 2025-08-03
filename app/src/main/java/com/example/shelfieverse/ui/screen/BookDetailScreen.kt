package com.example.shelfieverse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import coil.compose.AsyncImage

// Note: Add this dependency to build.gradle.kts if not already present:
// implementation("io.coil-kt:coil-compose:2.4.0")

// Data class for book details
data class BookDetail(
    val id: String = "",
    val name: String = "",
    val rating: Float = 0.0f,
    val releaseYear: String = "",
    val ageRating: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val genres: List<String> = emptyList(),
    val moral: String = ""
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    book: BookDetail,
    onBackClick: () -> Unit = {},
    onReadBookClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        book.name,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Book Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = book.imageUrl.ifEmpty { "https://via.placeholder.com/300x400?text=Book+Cover" },
                    contentDescription = "Book cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Book Name and Rating
                Column {
                    Text(
                        text = book.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700), // Gold color
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${book.rating}/10",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Genre Chips
                if (book.genres.isNotEmpty()) {
                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )

                    // Simple Row instead of FlowRow for now
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        book.genres.take(3).forEach { genre -> // Limit to 3 genres
                            AssistChip(
                                onClick = { },
                                label = { Text(genre) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }

                // Year and Age Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (book.releaseYear.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Release Year",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = book.releaseYear,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (book.ageRating.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Age Rating",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = book.ageRating,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Moral of the Book (if available)
                if (book.moral.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Moral of the Book",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = book.moral,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Description
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = book.description.ifEmpty { "No description available." },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp),
                            lineHeight = 20.sp
                        )
                    }
                }

                // Read Book Button
                Button(
                    onClick = onReadBookClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Read Book",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Add some space at the bottom
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Sample data for preview
fun getSampleBookDetail(): BookDetail {
    return BookDetail(
        id = "1",
        name = "The Great Gatsby",
        rating = 8.5f,
        releaseYear = "2023",
        ageRating = "PG13",
        description = "This is a detailed description of the movie explaining the plot, characters, and more. The text could be quite long, which is why we've placed it in a ScrollView to ensure all content is accessible. A classic American novel set in the Jazz Age, exploring themes of wealth, love, and the American Dream through the eyes of narrator Nick Carraway.",
        imageUrl = "",
        genres = listOf("Action", "Drama", "Classic"),
        moral = "The pursuit of the American Dream can lead to both great achievements and tragic downfalls, teaching us about the complexities of human ambition and love."
    )
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview() {
    BookDetailScreen(
        book = getSampleBookDetail()
    )
}