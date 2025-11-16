package uk.ac.tees.mad.hostelmanager.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.hostelmanager.presentation.auth.AuthScreen
import uk.ac.tees.mad.hostelmanager.presentation.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Menu : Screen("menu")
    object FileComplaint : Screen("file_complaint")
    object ComplaintStatus : Screen("complaint_status")
    object MyComplaints : Screen("my_complaints")
    object Rules : Screen("rules")
    object Profile : Screen("profile")
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
        /*
        composable(Screen.Menu.route) { MessMenuScreen(navController) }
        composable(Screen.FileComplaint.route) { FileComplaintScreen(navController) }
        composable(Screen.ComplaintStatus.route) { ComplaintStatusScreen(navController) }
        composable(Screen.MyComplaints.route) { MyComplaintsScreen(navController) }
        composable(Screen.Rules.route) { HostelRulesScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        */
    }
}