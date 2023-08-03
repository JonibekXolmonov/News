package bek.droid.news.data.repository

import bek.droid.news.common.SingleMapper
import bek.droid.news.data.mapper.ModelMapper
import bek.droid.news.data.model.response.Article
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.datasource.cache.CacheDataSource
import bek.droid.news.domain.datasource.local.LocalDataSource
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import bek.droid.news.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val cacheDataSource: CacheDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val articleMapper: SingleMapper<Article, ArticleModel>,
    private val dispatcher: CoroutineContext,
    private val modelMapper: ModelMapper,
) : MainRepository {

    private var page = 0
    override val allNewsCached: Flow<List<ArticleModel>>
        get() = localDataSource.getNews().map { it.map { entity -> modelMapper(entity) } }
            .flowOn(context = dispatcher)

    override suspend fun fetchUsBusinessNews(): List<ArticleModel> {
        page++
        val response = remoteDataSource.fetchUsBusinessNews(page)

        //save locally and returns inserted items' generated ids
        val insertedIDs = cacheDataSource.saveNews(response.articles)

        val modelList: List<ArticleModel> = response.articles.map { articleMapper.invoke(it) }

        return modelList
    }
}