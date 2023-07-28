package bek.droid.news.data.source

import bek.droid.news.common.SingleMapper
import bek.droid.news.data.db.NewsDao
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Article
import bek.droid.news.domain.datasource.cache.CacheDataSource

class CacheDataSourceImpl(
    val newsDao: NewsDao,
    private val entityMapper: SingleMapper<Article, NewsEntity>
) : CacheDataSource {
    override fun saveNews(news: List<Article>) {
        val entity = news.map { entityMapper.invoke(it) }
        newsDao.insertNews(entity)
    }
}