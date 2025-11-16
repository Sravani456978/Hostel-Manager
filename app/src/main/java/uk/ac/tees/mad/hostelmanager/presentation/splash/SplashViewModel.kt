package uk.ac.tees.mad.hostelmanager.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _nextScreen = MutableStateFlow<String?>(null)
    val nextScreen: StateFlow<String?> = _nextScreen

    init {
        viewModelScope.launch {
            delay(2000)
            val currentUser = auth.currentUser
            if (currentUser != null) {
                _nextScreen.value = Screen.Menu.route
            } else {
                _nextScreen.value = Screen.Auth.route
            }
        }
    }
}
