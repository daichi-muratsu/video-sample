package com.example.videosample.domain.repository

import com.example.videosample.domain.model.Video

interface VideoRepository {
    suspend fun getVideos(): List<Video>
    suspend fun getVideo(id: String): Video?
}
