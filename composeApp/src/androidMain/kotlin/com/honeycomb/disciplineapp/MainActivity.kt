package com.honeycomb.disciplineapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.initialize
import com.google.firebase.Firebase
import com.honeycomb.disciplineapp.presentation.focus_app.PlatformScreenTimeManager
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        screenTimeController = PlatformScreenTimeManager()


        Firebase.initialize(this)
        setContent {
            val activityContext = LocalContext.current
            CompositionLocalProvider(LocalPlatformContext provides activityContext) {
                App()
            }
        }
    }

    private lateinit var screenTimeController: PlatformScreenTimeManager

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        if (requestCode == 100) {
            val granted = grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

            // Send result back to KMP
            screenTimeController.onPermissionResult(granted)
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}