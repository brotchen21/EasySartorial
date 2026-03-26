package com.brotchen21.easysatorial.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PatternLibrary : Screen("pattern_library")
    object OutfitBuilder : Screen("outfit_builder")
    object RandomGenerator : Screen("random_generator")
}
