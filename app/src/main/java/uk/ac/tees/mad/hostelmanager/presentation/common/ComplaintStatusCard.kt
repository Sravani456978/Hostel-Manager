package uk.ac.tees.mad.hostelmanager.presentation.common

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.hostelmanager.domain.model.Complaint
import uk.ac.tees.mad.hostelmanager.ui.theme.AccentOrange
import uk.ac.tees.mad.hostelmanager.ui.theme.PrimaryBlue

@Composable
fun ComplaintStatusCard(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = complaint.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PrimaryBlue
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (!complaint.description.isNullOrEmpty()) {
                Text(
                    text = complaint.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (!complaint.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = complaint.photoUrl,
                    contentDescription = "Complaint Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${complaint.status}",
                    style = MaterialTheme.typography.bodySmall.copy(color = AccentOrange)
                )
                Text(
                    text = SimpleDateFormat("hh:mm a, dd/MM/yyyy").format(complaint.timestamp),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}
