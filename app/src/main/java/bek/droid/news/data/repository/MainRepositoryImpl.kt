package bek.droid.news.data.repository

import bek.droid.news.common.Constants.PAGE_SIZE
import bek.droid.news.common.SingleMapper
import bek.droid.news.data.model.response.Article
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

    private var page = 0
    private var pagesCount = 1
    override suspend fun fetchUsBusinessNews(): List<ArticleModel> {
        if (page + 1 <= pagesCount) {
            page++
            val response = remoteDataSource.fetchUsBusinessNews(page)

            //save locally
            cacheDataSource.saveNews(response.articles)

            pagesCount = response.totalResults / PAGE_SIZE + 1

            val modelList: List<ArticleModel> = response.articles
                .map { articleMapper.invoke(it) }

            return modelList
        }
        return emptyList()
    }
}