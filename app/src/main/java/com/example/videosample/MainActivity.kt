package com.example.videosample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.videosample.ui.navigation.VideoSampleNavDisplay
import com.example.videosample.ui.theme.VideoSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoSampleNavDisplay(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}