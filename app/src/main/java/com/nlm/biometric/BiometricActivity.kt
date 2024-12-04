package com.nlm.biometric

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nlm.ui.activity.DashboardActivity
import com.nlm.ui.activity.LoginActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.warroom.biometric.BiometricPromptManager
import com.warroom.ui.theme.NLMTheme

class ComposeActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if token exists
        val token = Utility.getPreferenceString(this, PrefEntities.TOKEN)

        // Redirect to login if token is not present
        if (token.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Render biometric screen
        setContent {
            NLMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val biometricResult by promptManager.promptResults.collectAsState(initial = null)

                    // Automatically show the biometric prompt when the screen is loaded
                    LaunchedEffect(Unit) {
                        promptManager.showBiometricPrompt(
                            title = "Unlock App",
                            description = "Authenticate to access your dashboard"
                        )
                    }

                    // Handle biometric result
                    LaunchedEffect(biometricResult) {
                        when (biometricResult) {
                            is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                                // Show toast for success
                                Toast.makeText(this@ComposeActivity, "Authentication Successful", Toast.LENGTH_SHORT).show()
                                // Navigate to Dashboard
                                startActivity(Intent(this@ComposeActivity, DashboardActivity::class.java))
                                finish()
                            }
                            is BiometricPromptManager.BiometricResult.AuthenticationError,
                            BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                                // Show toast for failure
                                Toast.makeText(this@ComposeActivity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                                finish() // Close the activity
                            }
                            is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                                if (Build.VERSION.SDK_INT >= 30) {
                                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                        putExtra(
                                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                        )
                                    }
                                    startActivity(enrollIntent)
                                    finish()
                                }
                            }
                            else -> Unit
                        }
                    }

                    // Optional: Placeholder UI for debug purposes
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // No UI required as Toast messages handle the feedback
                    }
                }
            }
        }
    }
}
