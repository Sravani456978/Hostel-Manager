package uk.ac.tees.mad.hostelmanager.domain.usecase

import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository

class SyncMealsUseCase(
    private val repository: MenuRepository
) {
    suspend operator fun invoke(isOnline: Boolean) {
        repository.syncMeals(isOnline)
    }
}
