package com.example.videosample.ui.videolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.videosample.ui.videolist.components.VideoCard

@Composable
fun VideoListScreen(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VideoListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is VideoListUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is VideoListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.videos) { video ->
                        VideoCard(
                            video = video,
                            onClick = { onClick(video.id) },
                        )
                    }
                }
            }

            is VideoListUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}