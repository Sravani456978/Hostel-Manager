package uk.ac.tees.mad.hostelmanager.presentation.rules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.hostelmanager.presentation.navigation.BottomNavBar
import uk.ac.tees.mad.hostelmanager.ui.theme.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostelRulesScreen(
    navController: NavController
) {

    val sections = listOf(
        InfoSection(
            title = "General Rules",
            items = listOf(
                "Maintain discipline and silence in hostel premises.",
                "No visitors allowed inside hostel rooms.",
                "Respect fellow hostel members and staff.",
                "Keep your room and common areas clean."
            )
        ),
        InfoSection(
            title = "Timings",
            items = listOf(
                "Breakfast: 7:00 AM - 9:00 AM",
                "Lunch: 1:00 PM - 2:30 PM",
                "Dinner: 8:00 PM - 9:30 PM",
                "Hostel Gate Closes: 10:30 PM"
            )
        ),
        InfoSection(
            title = "Contact Information",
            items = listOf(
                "Warden: Mr. Oliver (Mob: +1 987 6543 210)",
                "Security Desk: +1 123 4567 890",
                "Emergency: 911"
            )
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(PrimaryBlue, LightBlue)))
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Text(
                text = "Hostel Information",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .systemBarsPadding()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(sections) { section ->
                    ExpandableInfoCard(section = section)
                }
            }
        }
    }
}

@Composable
fun ExpandableInfoCard(section: InfoSection) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = section.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    section.items.forEach { item ->
                        Text(
                            text = "• $item",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

data class InfoSection(
    val title: String,
    val items: List<String>
)