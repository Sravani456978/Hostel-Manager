package uk.ac.tees.mad.hostelmanager.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Destination(val route: String, val title: String, val icon: ImageVector) {
    object Menu : Destination(Screen.Menu.route, "Menu", Icons.Default.List)
    object Rules : Destination(Screen.Rules.route, "Complaint", Icons.Default.Home)
    object Profile : Destination(Screen.Profile.route, "Settings", Icons.Default.Settings)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Destination.Menu,
        Destination.Rules,
        Destination.Profile
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
