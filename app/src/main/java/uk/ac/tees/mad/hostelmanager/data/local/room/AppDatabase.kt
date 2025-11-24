package uk.ac.tees.mad.hostelmanager.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MenuItemsEntity::class, ComplaintEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun complaintDao(): ComplaintDao
}
