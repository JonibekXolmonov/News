package bek.droid.news.data.network

import bek.droid.news.common.Constants.PAGE_SIZE
import bek.droid.news.data.model.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=3838fac035eb48ca9d3073e65c073901
    @GET("top-headlines")
    suspend fun getUsBusinessNews(
        @Query("country") country: String = "us",
        @Query("category") category: String = "business",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("apiKey") apiKey: String = "c59b7c5fe689453ebcfabf5eab2d79fd",
    ): Response<NewsResponse>

}