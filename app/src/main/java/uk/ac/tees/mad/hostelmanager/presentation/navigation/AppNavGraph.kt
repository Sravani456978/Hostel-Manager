package uk.ac.tees.mad.hostelmanager.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.ac.tees.mad.hostelmanager.presentation.auth.AuthScreen
import uk.ac.tees.mad.hostelmanager.presentation.complaint.ComplaintScreen
import uk.ac.tees.mad.hostelmanager.presentation.complaint.MyComplaintsScreen
import uk.ac.tees.mad.hostelmanager.presentation.complaints.ComplaintStatusScreen
import uk.ac.tees.mad.hostelmanager.presentation.menu.MessMenuScreen
import uk.ac.tees.mad.hostelmanager.presentation.profile.ProfileScreen
import uk.ac.tees.mad.hostelmanager.presentation.rules.HostelRulesScreen
import uk.ac.tees.mad.hostelmanager.presentation.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Menu : Screen("menu")
    object FileComplaint : Screen("file_complaint")
    object ComplaintStatus : Screen("complaint_status")
    object MyComplaints : Screen("my_complaints/{name}")
    fun passName(name: String): String = "my_complaints/$name"
    object Rules : Screen("rules")
    object Profile : Screen("profile")
}

@Composable
fun AppNavGraph(navController: NavHostController, onGoogleSignInClick: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                navController = navController,
                onGoogleSignInClick = { onGoogleSignInClick() }
            )
        }


        composable(Screen.Menu.route) { MessMenuScreen(navController) }
       composable(Screen.FileComplaint.route) {
           ComplaintScreen(navController = navController)
       }
        composable(Screen.Rules.route) { HostelRulesScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.ComplaintStatus.route) { ComplaintStatusScreen(navController) }
        composable(
            route = Screen.MyComplaints.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
            MyComplaintsScreen(navController, name)
        }
    }
}