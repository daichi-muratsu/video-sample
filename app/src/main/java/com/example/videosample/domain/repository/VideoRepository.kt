package com.example.videosample.domain.repository

import com.example.videosample.domain.model.Video

interface VideoRepository {
    /**
     * 動画の一覧を取得する。
     *
     * @return 動画のリスト
     */
    suspend fun getVideos(): List<Video>

    /**
     * 指定された ID に対応する動画の詳細を取得する。
     *
     * @param id 取得したい動画のID
     * @return 該当する動画データ。見つからない場合は null を返す。
     */
    suspend fun getVideo(id: String): Video?

    /**
     * 指定された動画に関連する動画のリストを取得する。
     * （例：同じカテゴリの動画や、おすすめ動画など）
     *
     * @param id 基準となる動画のID
     * @return 関連動画のリスト。関連動画がない場合は空のリストを返す。
     */
    suspend fun getRelatedVideos(id: String): List<Video>
}
