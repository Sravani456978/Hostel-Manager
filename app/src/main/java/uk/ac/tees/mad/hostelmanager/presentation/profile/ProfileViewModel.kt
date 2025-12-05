package uk.ac.tees.mad.hostelmanager.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import uk.ac.tees.mad.hostelmanager.data.remote.User
import uk.ac.tees.mad.hostelmanager.domain.repository.ComplaintRepository
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    application: Application
) : AndroidViewModel(application) {

    private val _profile = MutableStateFlow(User())
    val profile: MutableStateFlow<User> = _profile

    init {
        fetchUserDetails()
    }

    fun fetchUserDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    _profile.value = user ?: User()
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        }
    }
}