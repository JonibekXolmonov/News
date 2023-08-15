package bek.droid.news.domain.datasource.local

import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNews(): Flow<List<NewsEntity>>

    fun search(query: String): Flow<List<NewsEntity>>
}