package uk.ac.tees.mad.hostelmanager.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MenuItemsEntity>)

    @Query("SELECT * FROM menu_items")
    fun getAllMeals(): Flow<List<MenuItemsEntity>>
}
