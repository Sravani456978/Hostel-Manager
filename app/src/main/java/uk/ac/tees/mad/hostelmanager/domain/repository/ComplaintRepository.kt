package uk.ac.tees.mad.hostelmanager.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.hostelmanager.data.local.room.ComplaintEntity

interface ComplaintRepository {
    fun getComplaints(): Flow<List<ComplaintEntity>>
    suspend fun addComplaint(complaint: ComplaintEntity, isOnline: Boolean)
    suspend fun syncComplaints()
}
