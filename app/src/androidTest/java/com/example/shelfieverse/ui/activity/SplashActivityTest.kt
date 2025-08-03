package com.example.booktok.ui.activity

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SplashActivity>()

    @Test
    fun splashScreen_loadsSuccessfully() {
        // Test that the splash screen loads without crashing
        Thread.sleep(1000)
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysTagline() {
        Thread.sleep(1000)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Discover Your Next Great Read", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysBookIcon() {
        Thread.sleep(1000)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("📚").assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysLoadingText() {
        Thread.sleep(1000)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Loading", substring = true).assertIsDisplayed()
    }
}