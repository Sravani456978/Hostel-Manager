//package uk.ac.tees.mad.hostelmanager.presentation.navigation
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.List
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.List
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//
//sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
//    object Menu : Screen("menu", "Menu", Icons.Default.List)
//    object Complaint : Screen("complaint", "Complaint", Icons.Default.Home)
//    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
//}
//
//@Composable
//fun BottomNavBar(navController: NavController) {
//    val items = listOf(
//        Screen.Menu,
//        Screen.Complaint,
//        Screen.Settings
//    )
//
//    NavigationBar {
//        val navBackStackEntry = navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry.value?.destination?.route
//
//        items.forEach { screen ->
//            NavigationBarItem(
//                icon = { Icon(screen.icon, contentDescription = screen.title) },
//                label = { Text(screen.title) },
//                selected = currentRoute == screen.route,
//                onClick = {
//                    if (currentRoute != screen.route) {
//                        navController.navigate(screen.route) {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
//                    }
//                }
//            )
//        }
//    }
//}
