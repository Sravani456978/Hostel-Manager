package uk.ac.tees.mad.hostelmanager.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.hostelmanager.presentation.navigation.BottomNavBar
import uk.ac.tees.mad.hostelmanager.presentation.navigation.Screen
import uk.ac.tees.mad.hostelmanager.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profile.collectAsState()
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                loading = true
                viewModel.updateProfilePhoto(it, onPhotoUploaded = {
                    loading = false
                }, onPhotoUploadFailed = {
                    loading = false
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
            }
        }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = Color.White
                            ), modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(PrimaryBlue, LightBlue)))
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AccentOrange,
                        strokeWidth = 2.dp
                    )
                } else {
                    AsyncImage(
                        model = profileState.photoUrl,
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                    IconButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Photo",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            var editingName by remember { mutableStateOf(false) }
            var newName by rememberSaveable { mutableStateOf(profileState.name) }

            if (editingName) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentOrange,
                        focusedLabelColor = AccentOrange
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.updateName(newName)
                            editingName = false
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Save Name",
                                tint = AccentOrange
                            )
                        }
                    }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable { editingName = true }
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = profileState.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Name",
                        tint = AccentOrange,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = profileState.email,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(40.dp))

            ProfileActionButton("My Complaints") {
                if (profileState.name.isNotEmpty()) {
                    navController.navigate(Screen.MyComplaints.passName(profileState.name))
                } else {
                    navController.navigate(Screen.MyComplaints.route)
                }
            }
            ProfileActionButton("Complaint Status") {
                navController.navigate(Screen.ComplaintStatus.route)
            }
            ProfileActionButton("Logout") {
                viewModel.logout()
                navController.navigate(Screen.Auth.route) {
                    popUpTo(0)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "ℹ️ To create an instant complaint press any volume key",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Preview(showBackground = true, name = "Hostel Manager – Profile Screen")
@Composable
fun ProfileScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3F51B5), Color(0xFF64B5F6))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Profile",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(40.dp))

        // Profile Picture
        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "JD",
                    color = Color(0xFF3F51B5),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF6D00))
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Photo",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { }
        ) {
            Text(
                "John Doe",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit Name",
                tint = Color(0xFFFF6D00),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "john.doe@tees.ac.uk",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.9f)
        )

        Spacer(Modifier.height(60.dp))

        // Action Buttons
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("My Complaints", color = Color.White)
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Complaint Status", color = Color.White)
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout", color = Color.White)
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "To create an instant complaint press any volume key",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}