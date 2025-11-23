package uk.ac.tees.mad.hostelmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.hostelmanager.data.local.room.MealDao
import uk.ac.tees.mad.hostelmanager.data.mappers.toDomain
import uk.ac.tees.mad.hostelmanager.data.mappers.toEntity
import uk.ac.tees.mad.hostelmanager.data.remote.MenuItemRemote
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem
import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val dao: MealDao
) : MenuRepository {

    override fun getMeals(): Flow<List<MenuItem>> =
        dao.getAllMeals().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncMeals(isOnline: Boolean) {
        if (isOnline) {
            val snapshot = db.collection("meals").get().await()
            val remoteMeals = snapshot.toObjects(MenuItemRemote::class.java)

            val entities = remoteMeals.map { it.toDomain().toEntity() }
            dao.insertMeals(entities)
        }
    }
}
