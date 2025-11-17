package uk.ac.tees.mad.hostelmanager

import android.content.IntentSender
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.hostelmanager.presentation.auth.AuthViewModel
import uk.ac.tees.mad.hostelmanager.presentation.navigation.AppNavGraph
import uk.ac.tees.mad.hostelmanager.ui.theme.HostelManagerTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavHostController

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val signInClient = Identity.getSignInClient(this)
        val credential = signInClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            authViewModel.handleGoogleAuthResult(idToken, navController)
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

    fun startGoogleSignIn() {
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
            }
    }

}
