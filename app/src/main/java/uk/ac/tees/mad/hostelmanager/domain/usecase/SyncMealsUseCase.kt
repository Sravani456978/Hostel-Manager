package uk.ac.tees.mad.hostelmanager.domain.usecase

import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository

class SyncMealsUseCase(
    private val repository: MenuRepository
) {
    // Pass isOnline so that the repository knows whether to fetch from Firebase
    suspend operator fun invoke(isOnline: Boolean) {
        repository.syncMeals(isOnline)
    }
}
