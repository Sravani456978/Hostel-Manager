package uk.ac.tees.mad.hostelmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.hostelmanager.presentation.auth.AuthViewModel
import uk.ac.tees.mad.hostelmanager.presentation.navigation.AppNavGraph
import uk.ac.tees.mad.hostelmanager.ui.theme.HostelManagerTheme
import uk.ac.tees.mad.hostelmanager.utils.NetworkUtils

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavHostController

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val signInClient = Identity.getSignInClient(this)
        try {
            val credential = signInClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                authViewModel.handleGoogleAuthResult(idToken, navController)
            }
        } catch (e: ApiException) {
            if (e.statusCode == 16) {
                Toast.makeText(this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Sign-in error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HostelManagerTheme {
                navController = rememberNavController()
                authViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    onGoogleSignInClick = { startGoogleSignIn() }
                )
            }
        }
    }

    private fun startGoogleSignIn() {
        if (!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, "No internet connection. Please connect and try again.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        Identity.getSignInClient(this)
            .getSignInIntent(request)
            .addOnSuccessListener { pendingIntent ->
                val intentSenderRequest =
                    IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                launcher.launch(intentSenderRequest)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Failed to start sign-in: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
