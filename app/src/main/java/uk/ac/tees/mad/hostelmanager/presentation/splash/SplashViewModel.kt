// presentation/splash/SplashViewModel.kt
package uk.ac.tees.mad.hostelmanager.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    private val _nextScreen = MutableStateFlow("")
    val nextScreen: StateFlow<String> = _nextScreen

    init {
        viewModelScope.launch {
            delay(2000) 
            // TODO: check FirebaseAuth user here
            _nextScreen.value = Screen.Auth.route
        }
    }
}
