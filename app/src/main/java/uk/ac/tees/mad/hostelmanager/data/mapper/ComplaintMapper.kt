package uk.ac.tees.mad.hostelmanager.data.mappers

import uk.ac.tees.mad.hostelmanager.data.local.room.ComplaintEntity
import uk.ac.tees.mad.hostelmanager.data.remote.ComplaintRemote
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint

fun Complaint.toEntity() = ComplaintEntity(
    id = id,
    title = title,
    description = description,
    photoUrl = photoUrl,
    timestamp = timestamp
)

fun ComplaintEntity.toDomain() = Complaint(
    id = id,
    title = title,
    description = description,
    photoUrl = photoUrl,
    timestamp = timestamp
)

fun Complaint.toRemote() = ComplaintRemote(
    title = title,
    description = description,
    photoUrl = photoUrl,
    timestamp = timestamp
)

fun ComplaintRemote.toDomain() = Complaint(
    title = title,
    description = description,
    photoUrl = photoUrl,
    timestamp = timestamp
)
