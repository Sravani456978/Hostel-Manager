package uk.ac.tees.mad.hostelmanager.domain.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint

interface ComplaintUseCases {
    suspend fun fileComplaint(complaint: Complaint, isOnline: Boolean)
    fun getComplaints(): Flow<List<Complaint>>
}