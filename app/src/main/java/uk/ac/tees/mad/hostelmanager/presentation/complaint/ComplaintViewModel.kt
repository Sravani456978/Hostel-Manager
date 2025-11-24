package uk.ac.tees.mad.hostelmanager.presentation.complaint

import android.app.Application
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
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(
    private val repository: ComplaintRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints

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

    fun fileComplaint(title: String, description: String, photoUrl: String?) {
        val isOnline = NetworkUtils.isOnline(getApplication())
        viewModelScope.launch {
            val complaint = Complaint(title = title, description = description, photoUrl = photoUrl)
            repository.addComplaint(complaint.toEntity(), isOnline)
        }
    }

    fun uploadImageToCloudinary(
        filePath: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val config = hashMapOf(
            "cloud_name" to "dn8ycjojw",
            "api_key" to "281678982458183",
            "api_secret" to "77nO2JN3hkGXB-YgGZuJOqXcA4Q"
        )

        val cloudinary = Cloudinary(config)
        Thread {
            try {
                val result = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap())
                onSuccess(result["secure_url"].toString())
            } catch (e: Exception) {
                onError(e)
            }
        }.start()
    }
}
