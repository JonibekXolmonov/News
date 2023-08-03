package bek.droid.news.domain.datasource.cache

import bek.droid.news.data.model.response.Article
import kotlinx.coroutines.flow.Flow

interface CacheDataSource {
    fun saveNews(news: List<Article>): List<Long>
}