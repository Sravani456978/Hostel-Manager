package uk.ac.tees.mad.hostelmanager.presentation.profile

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.hostelmanager.domain.model.Profile
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val cloudinaryConfig = hashMapOf(
        "cloud_name" to "dn8ycjojw",
        "api_key" to "281678982458183",
        "api_secret" to "77nO2JN3hkGXB-YgGZuJOqXcA4Q"
    )
    private val cloudinary = Cloudinary(cloudinaryConfig)

    private val _profile = MutableStateFlow(Profile("", "", ""))
    val profile: StateFlow<Profile> = _profile

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                val name = snapshot.getString("name") ?: ""
                val email = snapshot.getString("email") ?: auth.currentUser?.email.orEmpty()
                val photoUrl = snapshot.getString("photoUrl") ?: ""

                _profile.value = Profile(name = name, email = email, photoUrl = photoUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateName(newName: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                firestore.collection("users").document(uid)
                    .update("name", newName).await()
                _profile.value = _profile.value.copy(name = newName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateProfilePhoto(uri: Uri, onPhotoUploaded: () -> Unit, onPhotoUploadFailed: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        Log.d("ProfileViewModel", "Uploading photo: $uri")
        viewModelScope.launch {
            try {

                val tempFile = withContext(Dispatchers.IO) {
                    val contentResolver: ContentResolver = getApplication<Application>().contentResolver
                    val inputStream = contentResolver.openInputStream(uri)
                        ?: throw IllegalStateException("Could not open InputStream for URI: $uri")
                    val file = File.createTempFile("profile_photo", ".jpg", getApplication<Application>().cacheDir)
                    FileOutputStream(file).use { outputStream ->
                        inputStream.use { it.copyTo(outputStream) }
                    }
                    file
                }

                val result = withContext(Dispatchers.IO) {
                    cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap())
                }

                withContext(Dispatchers.IO) {
                    tempFile.delete()
                }
                val photoUrl = result["secure_url"].toString()

                firestore.collection("users").document(uid)
                    .update("photoUrl", photoUrl).await()

                _profile.value = _profile.value.copy(photoUrl = photoUrl)
                onPhotoUploaded()
            } catch (e: Exception) {
                onPhotoUploadFailed(e.message)
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}
