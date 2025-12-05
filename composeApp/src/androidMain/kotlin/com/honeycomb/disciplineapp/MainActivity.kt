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
import com.honeycomb.disciplineapp.presentation.ui.AppTheme
import com.honeycomb.disciplineapp.presentation.ui.Theme
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)



        Firebase.initialize(this)
        setContent {
            val activityContext = LocalContext.current
            CompositionLocalProvider(
                LocalTheme provides Theme.getTheme(),
                LocalPlatformContext provides activityContext
            ) {
                App()
            }
        }
    }

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

        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}