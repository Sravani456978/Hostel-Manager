package uk.ac.tees.mad.hostelmanager.data.remote

data class ComplaintRemote(
    val title: String = "",
    val description: String = "",
    val photoUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "unresolved",
    val userId: String = ""
)
