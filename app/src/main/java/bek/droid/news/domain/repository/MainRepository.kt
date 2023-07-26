package bek.droid.news.domain.repository

import bek.droid.news.data.model.response.NewsResponse
import bek.droid.news.data.model.ui_model.ArticleModel

interface MainRepository {
    suspend fun fetchUsBusinessNews(): List<ArticleModel>
}