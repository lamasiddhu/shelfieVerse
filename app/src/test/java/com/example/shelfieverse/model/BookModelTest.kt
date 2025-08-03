package com.example.booktok.model

import org.junit.Test
import org.junit.Assert.*

class BookModelTest {

    @Test
    fun `book model creation with valid data should work`() {
        // Arrange - Create a test book with your exact property names
        val book = BookModel(
            BookId = "test-123",
            BookName = "Test Book",
            description = "A test book description",
            movielink = "https://example.com/movie",
            Rating = 4.5f,
            imageUrl = "https://example.com/test.jpg",
            releaseYear = "2023",  // String, not Int
            duration = "120 mins",
            ageRating = "PG-13",
            genres = listOf("Fiction", "Drama"),
            trailerUrl = "https://example.com/trailer"
        )

        // Assert - Check all properties are set correctly
        assertEquals("test-123", book.BookId)
        assertEquals("Test Book", book.BookName)
        assertEquals("A test book description", book.description)
        assertEquals("https://example.com/movie", book.movielink)
        assertEquals(4.5f, book.Rating, 0.01f)
        assertEquals("https://example.com/test.jpg", book.imageUrl)
        assertEquals("2023", book.releaseYear)
        assertEquals("120 mins", book.duration)
        assertEquals("PG-13", book.ageRating)
        assertEquals(2, book.genres.size)
        assertTrue(book.genres.contains("Fiction"))
        assertTrue(book.genres.contains("Drama"))
        assertEquals("https://example.com/trailer", book.trailerUrl)
    }

    @Test
    fun `book with empty optional fields should work`() {
        // Arrange - Create book with minimal data (using defaults)
        val book = BookModel(
            BookId = "simple-book",
            BookName = "Simple Book",
            description = "Simple description"
        )

        // Assert - Check defaults are applied
        assertEquals("simple-book", book.BookId)
        assertEquals("Simple Book", book.BookName)
        assertEquals("Simple description", book.description)
        assertEquals("", book.movielink)  // Default empty string
        assertEquals(0f, book.Rating, 0.01f)  // Default 0
        assertEquals("", book.imageUrl)  // Default empty string
        assertEquals("", book.releaseYear)  // Default empty string
        assertEquals("", book.duration)  // Default empty string
        assertEquals("", book.ageRating)  // Default empty string
        assertTrue(book.genres.isEmpty())  // Default empty list
        assertEquals("", book.trailerUrl)  // Default empty string
    }

    @Test
    fun `book with multiple genres should work correctly`() {
        // Arrange - Create book with many genres
        val book = BookModel(
            BookId = "multi-genre",
            BookName = "Multi Genre Book",
            description = "Book with multiple genres",
            Rating = 4.2f,
            releaseYear = "2022",
            ageRating = "PG-13",
            genres = listOf("Adventure", "Fantasy", "Young Adult", "Romance")
        )

        // Assert - Check all genres are present
        assertEquals(4, book.genres.size)
        assertTrue(book.genres.contains("Adventure"))
        assertTrue(book.genres.contains("Fantasy"))
        assertTrue(book.genres.contains("Young Adult"))
        assertTrue(book.genres.contains("Romance"))
    }

    @Test
    fun `book rating should handle different values`() {
        // Arrange - Create books with different ratings
        val lowRatingBook = BookModel(
            BookId = "low-rating",
            BookName = "Low Rating Book",
            Rating = 1.0f
        )

        val highRatingBook = BookModel(
            BookId = "high-rating",
            BookName = "High Rating Book",
            Rating = 5.0f
        )

        val decimalRatingBook = BookModel(
            BookId = "decimal-rating",
            BookName = "Decimal Rating Book",
            Rating = 3.7f
        )

        // Assert - Check ratings are stored correctly
        assertEquals(1.0f, lowRatingBook.Rating, 0.01f)
        assertEquals(5.0f, highRatingBook.Rating, 0.01f)
        assertEquals(3.7f, decimalRatingBook.Rating, 0.01f)
    }

    @Test
    fun `book release year should handle different formats`() {
        // Arrange - Test different year formats
        val book2023 = BookModel(
            BookId = "year-2023",
            BookName = "2023 Book",
            releaseYear = "2023"
        )

        val bookOld = BookModel(
            BookId = "year-old",
            BookName = "Old Book",
            releaseYear = "1925"
        )

        val bookEmpty = BookModel(
            BookId = "year-empty",
            BookName = "No Year Book",
            releaseYear = ""
        )

        // Assert - Check year strings are stored correctly
        assertEquals("2023", book2023.releaseYear)
        assertEquals("1925", bookOld.releaseYear)
        assertEquals("", bookEmpty.releaseYear)
    }
}