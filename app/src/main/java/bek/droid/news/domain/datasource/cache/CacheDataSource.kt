package bek.droid.news.domain.datasource.cache

import bek.droid.news.data.model.response.Article

interface CacheDataSource {
    fun saveNews(news: List<Article>)
}