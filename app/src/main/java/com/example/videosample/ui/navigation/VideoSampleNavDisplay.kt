package com.example.videosample.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.videosample.ui.videolist.VideoListScreen
import kotlinx.serialization.serializer

@Composable
fun rememberVideoSampleNavBackStack(vararg elements: VideoSampleNavKey): NavBackStack<VideoSampleNavKey> =
    rememberSerializable(serializer = serializer()) {
        NavBackStack(*elements)
    }

@Composable
fun VideoSampleNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberVideoSampleNavBackStack(VideoSampleNavKey.VideoList)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is VideoSampleNavKey.VideoList -> NavEntry(key) {
                    VideoListScreen(
                        onClick = { videoId ->
                            backStack.add(VideoSampleNavKey.VideoPlayer(videoId))
                        },
                        modifier = modifier,
                    )
                }

                is VideoSampleNavKey.VideoPlayer -> NavEntry(key) {
                    // TODO: VideoPlayerScreenに置き換える
                    Column(modifier = modifier.fillMaxSize()) {
                        Text(text = "videoId = ${key.videoId}")
                    }
                }
            }
        }
    )
}