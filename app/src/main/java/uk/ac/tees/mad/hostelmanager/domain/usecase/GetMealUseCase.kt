package uk.ac.tees.mad.hostelmanager.domain.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem
import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository

class GetMealsUseCase(
    private val repository: MenuRepository
) {
    operator fun invoke(): Flow<List<MenuItem>> = repository.getMeals()
}
