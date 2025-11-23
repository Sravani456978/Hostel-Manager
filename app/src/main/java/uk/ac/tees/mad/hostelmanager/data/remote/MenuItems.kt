package uk.ac.tees.mad.hostelmanager.data.remote

data class MenuItemRemote(
    val day: String = "",
    val breakfast: Dish = Dish(),
    val lunch: Dish = Dish(),
    val dinner: Dish = Dish()
)
