package bek.droid.news.domain.datasource.remote

import bek.droid.news.data.model.response.NewsResponse

interface RemoteDataSource {
    suspend fun fetchUsBusinessNews(): NewsResponse
}