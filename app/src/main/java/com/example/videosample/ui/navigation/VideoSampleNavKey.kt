package com.example.videosample.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface VideoSampleNavKey : NavKey {
    @Serializable
    data object VideoList : VideoSampleNavKey

    @Serializable
    data class VideoPlayer(val videoId: String) : VideoSampleNavKey
}