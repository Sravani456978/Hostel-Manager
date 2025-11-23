package uk.ac.tees.mad.hostelmanager.presentation.menu

import android.health.connect.datatypes.MealType
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.hostelmanager.R
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem
import uk.ac.tees.mad.hostelmanager.ui.theme.*

@Composable
fun MessMenuScreen(navController: NavController,
                   viewModel: MenuViewModel = hiltViewModel()) {
    val meals = viewModel.meals.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(PrimaryBlue, LightBlue))
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Mess Menu",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally).systemBarsPadding()
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(meals) { index ->
                ExpandableDayCard(meal = index)
            }
        }
    }
}

@Composable
fun ExpandableDayCard(meal: MenuItem) {
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
            Text(
                text = meal.day,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    MealRow("🍳 Breakfast", meal.breakfast.name, meal.breakfast.imageUrl)
                    MealRow("🥗 Lunch", meal.lunch.name, meal.lunch.imageUrl)
                    MealRow("🍛 Dinner", meal.dinner.name, meal.dinner.imageUrl)
                }
            }
        }
    }
}

@Composable
fun MealRow(label: String, name: String, imageRes: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
             imageRes,
            contentDescription = name,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.Bold, color = AccentOrange)
            Text(text = name, fontSize = 14.sp)
        }
    }
}
