// presentation/auth/AuthViewModel.kt
package uk.ac.tees.mad.hostelmanager.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun handleGoogleAuthResult(idToken: String?, navController: androidx.navigation.NavController) {
        if (idToken == null) {
            _uiState.value = AuthUiState(error = "Google ID Token null")
            return
        }

        _uiState.value = AuthUiState(isLoading = true)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserToFirestore(it.uid, it.displayName, it.email, it.photoUrl.toString())
                        navController.navigate(Screen.Menu.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                } else {
                    _uiState.value = AuthUiState(error = task.exception?.message)
                }
            }
    }

    private fun saveUserToFirestore(uid: String, name: String?, email: String?, photoUrl: String?) {
        val userDoc = hashMapOf(
            "uid" to uid,
            "name" to (name ?: ""),
            "email" to (email ?: ""),
            "photoUrl" to (photoUrl ?: "")
        )
        db.collection("users").document(uid).set(userDoc)
    }
}
