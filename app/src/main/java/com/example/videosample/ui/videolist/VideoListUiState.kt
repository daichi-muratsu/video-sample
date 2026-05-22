package com.example.videosample.ui.videolist

import com.example.videosample.domain.model.Video

/**
 * 動画一覧画面のUI状態を表現する Sealed Interface
 */
sealed interface VideoListUiState {
    /**
     * データ取得中の状態（初期状態）
     */
    data object Loading : VideoListUiState

    /**
     * データ取得成功の状態
     * @param videos 取得した動画のリスト
     */
    data class Success(val videos: List<Video>) : VideoListUiState

    /**
     * データ取得失敗の状態
     * @param message エラーの詳細なメッセージ
     */
    data class Error(val message: String) : VideoListUiState
}