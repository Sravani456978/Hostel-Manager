package uk.ac.tees.mad.hostelmanager.presentation.menu

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem
import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository
import uk.ac.tees.mad.hostelmanager.utils.NetworkUtils
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenuRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _meals = MutableStateFlow<List<MenuItem>>(emptyList())
    val meals: StateFlow<List<MenuItem>> = _meals

    private val order = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )

    init {
        fetchMeals()
    }

    fun fetchMeals() {
        viewModelScope.launch {
            val isOnline = NetworkUtils.isOnline(getApplication())

            try {
                // If device have internet then launch the sync this will store the data in room
                if (isOnline) repository.syncMeals(true)

                // Always collect from Room
                repository.getMeals().collect { localMeals ->
                    _meals.value = localMeals.sortedBy { order.indexOf(it.day) }
                }

            } catch (e: Exception) {
                Log.e("MenuViewModel", "Error fetching meals: ${e.message}", e)
            }
        }
    }
}
