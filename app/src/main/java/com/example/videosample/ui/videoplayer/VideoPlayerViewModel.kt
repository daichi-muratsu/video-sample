package com.example.videosample.ui.videoplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videosample.domain.model.Video
import com.example.videosample.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface VideoPlayerUiState {
    data object Loading : VideoPlayerUiState

    data class Success(
        val video: Video,
        val relatedVideos: List<Video>,
    ) : VideoPlayerUiState

    data class Error(val message: String) : VideoPlayerUiState
}

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: VideoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<VideoPlayerUiState>(VideoPlayerUiState.Loading)
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    /**
     * 指定された ID の動画および関連動画を読み込み、UI 状態を更新する。
     *
     * @param id 読み込む動画の ID
     */
    fun loadVideo(id: String) {
        viewModelScope.launch {
            _uiState.value = VideoPlayerUiState.Loading

            try {
                coroutineScope {
                    val videoDeferred = async { repository.getVideo(id) }
                    val relatedVideosDeferred = async { repository.getRelatedVideos(id) }

                    val video = videoDeferred.await()
                    val relatedVideos = relatedVideosDeferred.await()

                    if (video != null) {
                        _uiState.value = VideoPlayerUiState.Success(
                            video = video,
                            relatedVideos = relatedVideos,
                        )
                    } else {
                        _uiState.value = VideoPlayerUiState.Error("動画が見つかりませんでした")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = VideoPlayerUiState.Error("エラーが発生しました: ${e.message}")
            }
        }
    }
}