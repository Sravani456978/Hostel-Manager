package uk.ac.tees.mad.hostelmanager.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val photoUrl: String? = null,
    val timestamp: Long,
    val status: String = "unresolved",
    val userId: String
)
