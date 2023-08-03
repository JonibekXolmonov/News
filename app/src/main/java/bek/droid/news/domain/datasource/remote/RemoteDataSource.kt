package bek.droid.news.domain.datasource.remote

import bek.droid.news.common.Constants.DEFAULT_PAGE
import bek.droid.news.data.model.response.NewsResponse

interface RemoteDataSource {
    suspend fun fetchUsBusinessNews(page: Int = DEFAULT_PAGE): NewsResponse
}