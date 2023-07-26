package bek.droid.news.data.repository

import bek.droid.news.common.SingleMapper
import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.model.response.Article
import bek.droid.news.data.model.response.NewsResponse
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.datasource.cache.CacheDataSource
import bek.droid.news.domain.datasource.local.LocalDataSource
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import bek.droid.news.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val cacheDataSource: CacheDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val articleMapper: SingleMapper<Article, ArticleModel>
) : MainRepository {

    override suspend fun fetchUsBusinessNews(): List<ArticleModel> {
        val response = remoteDataSource.fetchUsBusinessNews()

        val entity: List<ArticleModel> = response.articles
            .map { articleMapper.invoke(it) }
        //save locally

        return entity
    }
}