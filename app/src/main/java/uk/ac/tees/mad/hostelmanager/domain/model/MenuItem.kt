package uk.ac.tees.mad.hostelmanager.domain.model

data class MenuItem(
    val day: String,
    val breakfast: Dish,
    val lunch: Dish,
    val dinner: Dish
)
