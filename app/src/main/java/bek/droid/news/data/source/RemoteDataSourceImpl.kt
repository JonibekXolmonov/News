package bek.droid.news.data.source

import bek.droid.news.data.model.response.NewsResponse
import bek.droid.news.data.network.ApiService
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    RemoteDataSource {
    override suspend fun fetchUsBusinessNews(page: Int): NewsResponse {
        val response = apiService.getUsBusinessNews(page = page)

        return when (response.code()) {
            200 -> response.body()!!
            else -> throw Exception(response.message())
        }
    }
}