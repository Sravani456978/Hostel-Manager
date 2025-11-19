package uk.ac.tees.mad.hostelmanager.presentation.menu

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
import uk.ac.tees.mad.hostelmanager.R
import uk.ac.tees.mad.hostelmanager.ui.theme.*

data class Meal(
    val day: String,
    val breakfast: Pair<String, Int>,
    val lunch: Pair<String, Int>,
    val dinner: Pair<String, Int>
)

private val sampleMenu = listOf(
    Meal(
        "Monday",
        "Pancakes & Coffee" to R.drawable.breakfast,
        "Grilled Chicken Sandwich & Fries" to R.drawable.lunch,
        "Spaghetti Bolognese with Garlic Bread" to R.drawable.dinner
    ),
    Meal(
        "Tuesday",
        "Bagel with Cream Cheese & Orange Juice" to R.drawable.breakfast,
        "Cheeseburger, Fries & Coleslaw" to R.drawable.lunch,
        "BBQ Ribs with Mashed Potatoes & Corn" to R.drawable.dinner
    ),
    Meal(
        "Wednesday",
        "Cereal with Milk & Banana" to R.drawable.breakfast,
        "Turkey Wrap & Salad" to R.drawable.lunch,
        "Grilled Salmon, Rice Pilaf & Veggies" to R.drawable.dinner
    ),
    Meal(
        "Thursday",
        "French Toast & Maple Syrup" to R.drawable.breakfast,
        "Chicken Caesar Salad & Breadsticks" to R.drawable.lunch,
        "Beef Tacos with Salsa & Guacamole" to R.drawable.dinner
    ),
    Meal(
        "Friday",
        "Omelette with Toast & Coffee" to R.drawable.breakfast,
        "Mac & Cheese with Garlic Bread" to R.drawable.lunch,
        "Pepperoni Pizza & Garden Salad" to R.drawable.dinner
    ),
    Meal(
        "Saturday",
        "Waffles with Berries & Syrup" to R.drawable.breakfast,
        "Hot Dogs with Fries" to R.drawable.lunch,
        "Fried Chicken with Mashed Potatoes & Gravy" to R.drawable.dinner
    ),
    Meal(
        "Sunday",
        "Scrambled Eggs, Sausage & Toast" to R.drawable.breakfast,
        "Club Sandwich with Chips" to R.drawable.lunch,
        "Steak with Roasted Veggies & Baked Potato" to R.drawable.dinner
    )
)

@Composable
fun MessMenuScreen(navController: NavController,
                   viewModel: MenuViewModel = hiltViewModel()) {
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
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleMenu.size) { index ->
                ExpandableDayCard(meal = sampleMenu[index])
            }
        }
    }
}

@Composable
fun ExpandableDayCard(meal: Meal) {
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
                    MealRow("🍳 Breakfast", meal.breakfast.first, meal.breakfast.second)
                    MealRow("🥗 Lunch", meal.lunch.first, meal.lunch.second)
                    MealRow("🍛 Dinner", meal.dinner.first, meal.dinner.second)
                }
            }
        }
    }
}

@Composable
fun MealRow(label: String, name: String, imageRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = imageRes),
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
