package com.example.shelfieverse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelfieverse.ViewModel.BookViewModel
import com.example.shelfieverse.repository.BookRepositoryImp
import kotlinx.coroutines.delay

data class Book(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val genre: String = "",
    val publicationYear: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDashboardScreen(
    onAddBookClick: () -> Unit = {},
    onBookClick: (Book) -> Unit = {},
    onEditBookClick: (Book) -> Unit = {}
) {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookToDelete by remember { mutableStateOf<Book?>(null) }

    val bookViewModel = remember { BookViewModel(BookRepositoryImp()) }

    LaunchedEffect(refreshTrigger) {
        isLoading = true
        delay(2000)

        val repo = BookRepositoryImp()
        repo.getAllBook { firebaseBooks, success, message ->
            if (success && firebaseBooks != null) {
                books = firebaseBooks.map { bookModel ->
                    Book(
                        id = bookModel.BookId,
                        title = bookModel.BookName,
                        author = "Unknown Author",
                        description = bookModel.description,
                        genre = bookModel.genres.firstOrNull() ?: "",
                        publicationYear = bookModel.releaseYear
                    )
                }
            } else {
                books = getSampleBooks()
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Books", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { refreshTrigger++ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBookClick, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Add Book", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(modifier = Modifier.size(48.dp), color = MaterialTheme.colorScheme.primary)
                            Text("Loading books from Firebase...", modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                books.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                            Text("📚", fontSize = 64.sp, modifier = Modifier.padding(bottom = 16.dp))
                            Text("No Books in Firebase!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("Add your first book to see it here", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))
                            Button(onClick = onAddBookClick, modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text("Add Your First Book")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(onClick = { refreshTrigger++ }, modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text("Refresh from Firebase")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(books) { book ->
                            BookItem(
                                book = book,
                                onItemClick = { onBookClick(book) },
                                onEditClick = { onEditBookClick(book) },
                                onDeleteClick = {
                                    bookToDelete = book
                                    showDeleteDialog = true
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && bookToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                bookToDelete = null
            },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete \"${bookToDelete?.title}\"? This will delete it from Firebase too.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookToDelete?.let { book ->
                            bookViewModel.deleteBook(book.id) { success, _ ->
                                if (success) {
                                    refreshTrigger++
                                }
                            }
                        }
                        showDeleteDialog = false
                        bookToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    bookToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BookItem(
    book: Book,
    onItemClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onItemClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(book.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (book.author.isNotEmpty()) {
                        Text("by ${book.author}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                    }
                    if (book.description.isNotEmpty()) {
                        Text(book.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 8.dp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (book.genre.isNotEmpty()) {
                            Text(
                                text = book.genre,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        if (book.publicationYear.isNotEmpty()) {
                            Text(book.publicationYear, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Column {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

fun getSampleBooks(): List<Book> {
    return listOf(
        Book(
            id = "sample1",
            title = "Sample Book 1",
            author = "Sample Author",
            description = "This is sample data. Real books will load from Firebase.",
            genre = "Sample",
            publicationYear = "2024"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun BookDashboardScreenPreview() {
    BookDashboardScreen()
}
