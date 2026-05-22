package com.example.videosample.ui.videolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videosample.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoListUiState>(VideoListUiState.Loading)
    val uiState: StateFlow<VideoListUiState> = _uiState.asStateFlow()

    init {
        fetchVideos()
    }

    /**
     * リポジトリから動画リストを取得し、結果に応じて画面状態（uiState）を更新する。
     */
    private fun fetchVideos() {
        viewModelScope.launch {
            // 再取得（Pull to Refreshなど）を考慮し、処理開始時にLoadingへ戻す
            _uiState.value = VideoListUiState.Loading

            try {
                val videos = repository.getVideos()
                _uiState.value = VideoListUiState.Success(videos)
            } catch (e: Exception) {
                _uiState.value = VideoListUiState.Error(e.message ?: "予期せぬエラーが発生しました")
            }
        }
    }
}