package uk.ac.tees.mad.hostelmanager.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Dish(
    val name: String = "",
    val imageUrl: String = ""
)

data class MenuItem(
    val day: String = "",
    val breakfast: Dish = Dish(),
    val lunch: Dish = Dish(),
    val dinner: Dish = Dish()
)

object MenuSeeder {
    private const val TAG = "MenuSeeder"

    suspend fun seedWeeklyMenu(db: FirebaseFirestore) {
        try {
            Log.d(TAG, "Seeding weekly menu...")
            val menus = listOf(
                MenuItem(
                    day = "Monday",
                    breakfast = Dish("Pancakes & Coffee", "https://images.unsplash.com/photo-1587734932263-2ec1b1c6f9ab"),
                    lunch = Dish("Grilled Chicken Sandwich & Fries", "https://images.unsplash.com/photo-1550547660-d9450f859349"),
                    dinner = Dish("Spaghetti Bolognese with Garlic Bread", "https://images.unsplash.com/photo-1604908815795-02e4a74d2fcb")
                ),
                MenuItem(
                    day = "Tuesday",
                    breakfast = Dish("Bagel with Cream Cheese & Orange Juice", "https://images.unsplash.com/photo-1553163147-622ab57e6f30"),
                    lunch = Dish("Cheeseburger, Fries & Coleslaw", "https://images.unsplash.com/photo-1606756791565-6741f7ee2e40"),
                    dinner = Dish("BBQ Ribs with Mashed Potatoes & Corn", "https://images.unsplash.com/photo-1604908815795-02e4a74d2fcb")
                ),
                MenuItem(
                    day = "Wednesday",
                    breakfast = Dish("Cereal with Milk & Banana", "https://images.unsplash.com/photo-1551183053-bf91a1d81141"),
                    lunch = Dish("Turkey Wrap & Salad", "https://images.unsplash.com/photo-1613145993483-5ee9e4cd09a7"),
                    dinner = Dish("Grilled Salmon, Rice Pilaf & Veggies", "https://images.unsplash.com/photo-1604908815830-1c826f3a11d4")
                ),
                MenuItem(
                    day = "Thursday",
                    breakfast = Dish("French Toast & Maple Syrup", "https://images.unsplash.com/photo-1601315480098-5d843f3d61a3"),
                    lunch = Dish("Chicken Caesar Salad & Breadsticks", "https://images.unsplash.com/photo-1613145993652-b4f76b5bb72b"),
                    dinner = Dish("Beef Tacos with Salsa & Guacamole", "https://images.unsplash.com/photo-1601924582971-d2f6e8c10f5f")
                ),
                MenuItem(
                    day = "Friday",
                    breakfast = Dish("Omelette with Toast & Coffee", "https://images.unsplash.com/photo-1559628233-b9fdd5a2c81f"),
                    lunch = Dish("Mac & Cheese with Garlic Bread", "https://images.unsplash.com/photo-1606756791565-6741f7ee2e40"),
                    dinner = Dish("Pepperoni Pizza & Garden Salad", "https://images.unsplash.com/photo-1548365328-8b3d8e2d97a8")
                ),
                MenuItem(
                    day = "Saturday",
                    breakfast = Dish("Waffles with Berries & Syrup", "https://images.unsplash.com/photo-1509440159596-0249088772ff"),
                    lunch = Dish("Hot Dogs with Fries", "https://images.unsplash.com/photo-1601924582971-d2f6e8c10f5f"),
                    dinner = Dish("Fried Chicken with Mashed Potatoes & Gravy", "https://images.unsplash.com/photo-1606756792461-4f1b0c4f3f3d")
                ),
                MenuItem(
                    day = "Sunday",
                    breakfast = Dish("Scrambled Eggs, Sausage & Toast", "https://images.unsplash.com/photo-1601314007429-9420cbd77a16"),
                    lunch = Dish("Club Sandwich with Chips", "https://images.unsplash.com/photo-1604908177620-89ecb9f3b1b0"),
                    dinner = Dish("Steak with Roasted Veggies & Baked Potato", "https://images.unsplash.com/photo-1604908177655-2c4f4e94d2a4")
                )
            )

            menus.forEach { menuItem ->
                try {
                    db.collection("meals")
                        .document(menuItem.day)
                        .set(menuItem)
                        .await()
                    Log.d(TAG, "✅ Seeded ${menuItem.day}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error seeding ${menuItem.day}: ${e.message}", e)
                }
            }
            Log.d(TAG, "Seeding completed successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error during seeding: ${e.message}", e)
        }
    }
}