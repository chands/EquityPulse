package com.equitypulse.presentation.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.equitypulse.R
import com.equitypulse.presentation.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bookmarks,
        BottomNavItem.Settings
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: androidx.navigation.NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = { 
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.screen.route
        } == true,
        onClick = {
            navController.navigate(screen.screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: Int
) {
    object Home : BottomNavItem(
        screen = Screen.Home,
        title = "Home",
        icon = R.drawable.ic_home
    )
    
    object Bookmarks : BottomNavItem(
        screen = Screen.Bookmarks,
        title = "Bookmarks",
        icon = R.drawable.ic_bookmark
    )
    
    object Settings : BottomNavItem(
        screen = Screen.Settings,
        title = "Settings",
        icon = R.drawable.ic_settings
    )
}
