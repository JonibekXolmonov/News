package bek.droid.news.data.source

import bek.droid.news.data.db.NewsDao
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.domain.datasource.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val newsDao: NewsDao) : LocalDataSource {

    override fun getNews(): Flow<List<NewsEntity>> {
        return newsDao.getNews()
    }

    override fun search(query: String): Flow<List<NewsEntity>> {
        return newsDao.search(query)
    }

}