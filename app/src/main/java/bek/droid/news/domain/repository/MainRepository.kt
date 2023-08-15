package bek.droid.news.domain.repository

import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun fetchUsBusinessNews(): List<ArticleModel>

     fun search(query: String):Flow<List<ArticleModel>>

    val allNewsCached: Flow<List<ArticleModel>>

}