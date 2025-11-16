package uk.ac.tees.mad.hostelmanager.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.hostelmanager.R
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val nextScreen by viewModel.nextScreen.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.app_icon),
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp).clip(RoundedCornerShape(34.dp))
                .shadow(5.dp)
        )
    }
    LaunchedEffect(nextScreen) {
        if (nextScreen != null) {
            navController.navigate(nextScreen!!) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

}
