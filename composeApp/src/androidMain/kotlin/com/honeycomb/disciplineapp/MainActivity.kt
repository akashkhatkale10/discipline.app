package com.honeycomb.disciplineapp

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
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        setContent {
            val activityContext = LocalContext.current
            CompositionLocalProvider(LocalPlatformContext provides activityContext) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}