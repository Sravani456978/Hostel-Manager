package uk.ac.tees.mad.hostelmanager.presentation.complaint

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.hostelmanager.presentation.common.ComplaintStatusCard
import uk.ac.tees.mad.hostelmanager.presentation.navigation.BottomNavBar
import uk.ac.tees.mad.hostelmanager.ui.theme.LightBlue
import uk.ac.tees.mad.hostelmanager.ui.theme.PrimaryBlue

@Composable
fun MyComplaintsScreen(navController: NavHostController,
                       viewModel: ComplaintViewModel = hiltViewModel()
) {
    val complaints by viewModel.myComplaints.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(PrimaryBlue, LightBlue)))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (complaints.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No complaints found",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null,
                        modifier = Modifier.systemBarsPadding().size(32.dp).clickable{
                            navController.popBackStack()
                        }, tint = Color.White)
                    Column {
                        Text(
                            text = "My Complaints",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .systemBarsPadding()
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(complaints) { complaint ->
                                ComplaintStatusCard(complaint)
                            }
                        }
                    }
                }
            }
        }
    }
}