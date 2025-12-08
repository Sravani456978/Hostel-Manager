package uk.ac.tees.mad.hostelmanager.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.hostelmanager.data.local.room.ComplaintDao
import uk.ac.tees.mad.hostelmanager.data.local.room.ComplaintEntity
import uk.ac.tees.mad.hostelmanager.data.remote.ComplaintRemote
import uk.ac.tees.mad.hostelmanager.domain.repository.ComplaintRepository
import javax.inject.Inject

class ComplaintRepositoryImpl @Inject constructor(
    private val dao: ComplaintDao,
    private val firestore: FirebaseFirestore
) : ComplaintRepository {

    override fun getComplaints(): Flow<List<ComplaintEntity>> = dao.getAllComplaints()

    override suspend fun addComplaint(complaint: ComplaintEntity, isOnline: Boolean) {
        dao.insertComplaint(complaint)
        if (isOnline) {
            firestore.collection("complaints").add(
                ComplaintRemote(
                    title = complaint.title,
                    description = complaint.description,
                    photoUrl = complaint.photoUrl,
                    timestamp = complaint.timestamp,
                    status = complaint.status,
                    userId = complaint.userId
                )
            ).await()
        }
    }

    override suspend fun syncComplaints() {
        val snapshot = firestore.collection("complaints").get().await()
        Log.d("ComplaintRepository", "Syncing ${snapshot.size()} complaints")
        val complaints = snapshot.documents.mapNotNull { doc ->
            doc.toObject(ComplaintRemote::class.java)?.let {
                ComplaintEntity(
                    id = 0,
                    title = it.title,
                    description = it.description,
                    photoUrl = it.photoUrl,
                    timestamp = it.timestamp,
                    status = it.status,
                    userId = it.userId
                )
            }
        }
        dao.clearComplaints()
        complaints.forEach { dao.insertComplaint(it) }
    }
}
