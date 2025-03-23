package com.equitypulse.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{newsId}") {
        fun createRoute(newsId: String) = "detail_screen/$newsId"
    }
    object Bookmarks : Screen("bookmarks_screen")
    object Settings : Screen("settings_screen")
}
