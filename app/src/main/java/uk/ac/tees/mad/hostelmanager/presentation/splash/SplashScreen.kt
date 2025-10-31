package uk.ac.tees.mad.hostelmanager.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.hostelmanager.R
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val navigateTo = viewModel.nextScreen.collectAsState()

    LaunchedEffect(navigateTo.value) {
        if (navigateTo.value.isNotEmpty()) {
            navController.navigate(navigateTo.value) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp)
        )
    }
}
