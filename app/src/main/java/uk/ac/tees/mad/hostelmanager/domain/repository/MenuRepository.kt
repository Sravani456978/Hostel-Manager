package uk.ac.tees.mad.hostelmanager.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem

interface MenuRepository {
    fun getMeals(): Flow<List<MenuItem>>
    suspend fun syncMeals(isOnline: Boolean)
}
