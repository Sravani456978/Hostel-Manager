package uk.ac.tees.mad.hostelmanager.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemsEntity(
    @PrimaryKey val day: String,
    val breakfastName: String,
    val breakfastImage: String,
    val lunchName: String,
    val lunchImage: String,
    val dinnerName: String,
    val dinnerImage: String
)
