package com.romnan.chillax.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.romnan.chillax.NavGraphs
import com.romnan.chillax.core.presentation.theme.ChillaxTheme
import logcat.logcat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillaxTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}