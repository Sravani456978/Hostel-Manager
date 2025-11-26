package uk.ac.tees.mad.hostelmanager.presentation.complaint

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint
import uk.ac.tees.mad.hostelmanager.domain.repository.ComplaintRepository
import uk.ac.tees.mad.hostelmanager.utils.NetworkUtils
import uk.ac.tees.mad.hostelmanager.data.mappers.toEntity
import uk.ac.tees.mad.hostelmanager.data.mappers.toDomain
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(
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
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var photoUrl: String? = null

                if (imageUri != null) {
                    val file = getFileFromUri(context, imageUri)
                    if (file != null) {
                        Thread {
                            try {
                                val config = hashMapOf(
                                    "cloud_name" to "dn8ycjojw",
                                    "api_key" to "281678982458183",
                                    "api_secret" to "77nO2JN3hkGXB-YgGZuJOqXcA4Q"
                                )
                                val cloudinary = Cloudinary(config)
                                val result = cloudinary.uploader().upload(file.absolutePath, ObjectUtils.emptyMap())
                                photoUrl = result["secure_url"].toString()

                                // File complaint after upload
                                fileComplaint(title, description, photoUrl)
                                onSuccess()
                            } catch (e: Exception) {
                                onError(e)
                            }
                        }.start()
                    } else {
                        onError(Exception("Failed to read image"))
                    }
                } else {
                    fileComplaint(title, description, null)
                    onSuccess()
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }


    private fun fileComplaint(title: String, description: String, photoUrl: String?) {
        val isOnline = NetworkUtils.isOnline(getApplication())
        viewModelScope.launch {
            val complaint = Complaint(title = title, description = description, photoUrl = photoUrl)
            repository.addComplaint(complaint.toEntity(), isOnline)
        }
    }
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output -> inputStream.copyTo(output) }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
