package com.example.videosample.data.repository

import com.example.videosample.domain.model.Video
import com.example.videosample.domain.repository.VideoRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 開発初期フェーズ用のモックリポジトリ。
 * API 実装を待たずに UI 構築を進めるため、固定の仮データを返却する。
 */
class MockVideoRepositoryImpl @Inject constructor() : VideoRepository {

    /**
     * [VIDEO_COUNT] 件の仮の動画リストを取得する。
     * ネットワーク通信をシミュレートするため、一定時間の遅延を発生させる。
     *
     * @return [VIDEO_COUNT] 件の仮データで構成された [Video] のリスト
     */
    override suspend fun getVideos(): List<Video> {
        simulateNetworkDelay()
        return (1..VIDEO_COUNT).map { id ->
            createMockVideo(id.toString())
        }
    }

    /**
     * 指定された ID に対応する仮の動画詳細を取得する。
     * ID が 1 から [VIDEO_COUNT] の範囲外である場合は null を返す。
     *
     * @param id 取得したい動画の ID
     * @return 該当する [Video] モデル、または null
     */
    override suspend fun getVideo(id: String): Video? {
        simulateNetworkDelay()

        val idInt = id.toIntOrNull()
        return if (idInt in 1..VIDEO_COUNT) {
            createMockVideo(id)
        } else {
            null
        }
    }

    /**
     * 指定された ID の動画に対する関連動画リストを取得する。
     * モック実装のため、[VIDEO_COUNT] 件の仮データから指定された ID の動画を除外したリストを返却する。
     *
     * @param id 基準となる動画のID
     * @return 指定された動画を除いた仮の関連動画リスト
     */
    override suspend fun getRelatedVideos(id: String): List<Video> {
        simulateNetworkDelay()

        return (1..VIDEO_COUNT)
            .map { createMockVideo(it.toString()) }
            .filter { it.id != id }
    }

    /**
     * ネットワーク通信をシミュレートするための遅延処理
     */
    private suspend fun simulateNetworkDelay() {
        delay(NETWORK_DELAY_MS)
    }

    /**
     * 指定された ID に基づいて仮の Video オブジェクトを生成する
     */
    private fun createMockVideo(id: String): Video {
        return Video(
            id = id,
            title = "Video $id",
            description = "Description for Video $id. This is a placeholder for testing UI layout.",
            thumbnailUrl = "https://picsum.photos/seed/$id/640/360", // UI開発用のダミー画像（スクロール時に画像が変わらないよう動画IDをシード値に設定）
            videoUrl = TEST_VIDEO_URL,
        )
    }

    companion object {
        private const val VIDEO_COUNT = 10
        private const val NETWORK_DELAY_MS = 1000L

        // 再生テスト用のHLS（ストリーミング形式）動画URL
        private const val TEST_VIDEO_URL = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
    }
}
