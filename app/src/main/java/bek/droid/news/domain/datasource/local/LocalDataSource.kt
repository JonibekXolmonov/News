package bek.droid.news.domain.datasource.local

import bek.droid.news.data.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNews(): Flow<List<NewsEntity>>
}