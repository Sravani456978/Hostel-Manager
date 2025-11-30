package uk.ac.tees.mad.hostelmanager.presentation.complaint

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint
import uk.ac.tees.mad.hostelmanager.domain.repository.ComplaintRepository
import uk.ac.tees.mad.hostelmanager.utils.NetworkUtils
import uk.ac.tees.mad.hostelmanager.data.mappers.toEntity
import uk.ac.tees.mad.hostelmanager.data.mappers.toDomain
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: ComplaintRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints

    private val cloudinaryConfig = hashMapOf(
        "cloud_name" to "dn8ycjojw",
        "api_key" to "281678982458183",
        "api_secret" to "77nO2JN3hkGXB-YgGZuJOqXcA4Q"
    )
    private val cloudinary = Cloudinary(cloudinaryConfig)

    init {
        fetchComplaints()
    }

    private fun fetchComplaints() {
        viewModelScope.launch {
            repository.getComplaints().collect { list ->
                _complaints.value = list.map { it.toDomain() }
            }
        }
    }

    fun submitComplaint(
        context: Context,
        title: String,
        description: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var photoUrl: String? = null

                if (imageUri != null) {
                    val file = getFileFromUri(context, imageUri)
                    if (file == null) {
                        onError("Failed to read image file")
                        return@launch
                    }
                    Log.d("ComplaintViewModel", "Uploading file: ${file.absolutePath}")

                    photoUrl = withContext(Dispatchers.IO) {
                        try {
                            val result = cloudinary.uploader().upload(file.absolutePath, ObjectUtils.emptyMap())
                            result["secure_url"]?.toString()
                        } catch (e: Exception) {
                            throw e
                        }
                    }

                    if (photoUrl == null) {
                        onError("Failed to get image URL from Cloudinary")
                        return@launch
                    }
                    Log.d("ComplaintViewModel", "Image uploaded successfully: $photoUrl")
                }

                fileComplaint(title, description, photoUrl)
                Log.d("ComplaintViewModel", "Complaint submitted successfully")
                onSuccess()
            } catch (e: Exception) {
                Log.e("ComplaintViewModel", "Submission failed: ${e.message}", e)
                onError("Submission failed: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private suspend fun fileComplaint(title: String, description: String, photoUrl: String?) {
        val isOnline = NetworkUtils.isOnline(getApplication())
        val complaint = Complaint(
            title = title,
            description = description,
            photoUrl = photoUrl,
            status = "unresolved",
            userId = auth.currentUser?.uid ?: ""
        )
        repository.addComplaint(complaint.toEntity(), isOnline)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output -> inputStream.copyTo(output) }
            Log.d("ComplaintViewModel", "File created: ${tempFile.absolutePath}")
            tempFile
        } catch (e: Exception) {
            Log.e("ComplaintViewModel", "Error in getFileFromUri: ${e.message}", e)
            null
        }
    }
}