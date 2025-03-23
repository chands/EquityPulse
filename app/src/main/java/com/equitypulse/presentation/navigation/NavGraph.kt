package com.equitypulse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.equitypulse.presentation.screens.bookmarks.BookmarksScreen
import com.equitypulse.presentation.screens.detail.NewsDetailScreen
import com.equitypulse.presentation.screens.home.HomeScreen
import com.equitypulse.presentation.screens.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNewsClick = { newsId ->
                    navController.navigate(Screen.Detail.createRoute(newsId))
                },
                onNavigateToBookmarks = {
                    navController.navigate(Screen.Bookmarks.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("newsId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailScreen(
                newsId = newsId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Bookmarks.route) {
            BookmarksScreen(
                onNewsClick = { newsId ->
                    navController.navigate(Screen.Detail.createRoute(newsId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
