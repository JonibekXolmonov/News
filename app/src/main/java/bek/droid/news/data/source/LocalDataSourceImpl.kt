package bek.droid.news.data.source

import bek.droid.news.data.db.NewsDao
import bek.droid.news.domain.datasource.local.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val newsDao: NewsDao) : LocalDataSource {

}