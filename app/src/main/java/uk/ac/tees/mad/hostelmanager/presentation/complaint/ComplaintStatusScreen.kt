package uk.ac.tees.mad.hostelmanager.presentation.complaints

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint
import uk.ac.tees.mad.hostelmanager.presentation.complaint.ComplaintViewModel
import uk.ac.tees.mad.hostelmanager.presentation.navigation.BottomNavBar
import uk.ac.tees.mad.hostelmanager.ui.theme.*

@Composable
fun ComplaintStatusScreen(
    navController: NavHostController,
    viewModel: ComplaintViewModel = hiltViewModel()
) {
    val complaints by viewModel.complaints.collectAsState()

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Text(
                        text = "Complaints",
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

@Composable
fun ComplaintStatusCard(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = complaint.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PrimaryBlue
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (!complaint.description.isNullOrEmpty()) {
                Text(
                    text = complaint.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (!complaint.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = complaint.photoUrl,
                    contentDescription = "Complaint Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${complaint.status}",
                    style = MaterialTheme.typography.bodySmall.copy(color = AccentOrange)
                )
                Text(
                    text = SimpleDateFormat("hh:mm a, dd/MM/yyyy").format(complaint.timestamp),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}
