package uk.ac.tees.mad.hostelmanager.domain.model

data class Complaint(
    val id: Int = 0,
    val title: String,
    val description: String,
    val photoUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "unresolved",
    val userId: String
)
