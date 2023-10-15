package bek.droid.news.data.repository

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.mapper.ModelMapper
import bek.droid.news.data.mapper.SingleMapper
import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.ReadLaterNewsEntity
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
    private val importantToModelMapper: SingleMapper<ImportantNewsEntity, ArticleModel>,
    private val readLaterToModelMapper: SingleMapper<ReadLaterNewsEntity, ArticleModel>

) : MainRepository {

    private var page = 0
    override fun getDbNews(): Flow<List<ArticleModel>> {
        return localDataSource.getNews().map {
            it.map { entity -> modelMapper(entity) }
        }
            .flowOn(context = dispatcher)
    }

    override suspend fun addToBookmark(
        articleModel: ArticleModel
    ): Result<Boolean> {
        return try {
            localDataSource.addToBookmark(articleModel)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromBookmark(articleModel: ArticleModel): Result<Boolean> {
        return try {
            localDataSource.removeFromBookmark(articleModel)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun existInLaterRead(id: Int): Boolean {
        return localDataSource.countFromReadLater(id) != 0
    }

    override suspend fun existInImportant(id: Int): Boolean {
        return localDataSource.countFromImportant(id) != 0
    }

    override suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState {
        return localDataSource.saveToReadLaterCollection(newsModel)
    }

    override suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState {
        return localDataSource.saveToImportantCollection(newsModel)
    }

    override fun savedNews(filter: SavedNewsFilter): Flow<List<ArticleModel>> {
        return when (filter) {
            SavedNewsFilter.IMPORTANT -> {
                val important = localDataSource.importantNews()
                    .map { it.map { importantToModelMapper.invoke(it) } }
                    .flowOn(context = dispatcher)
                important
            }

            SavedNewsFilter.READ_LATER -> {
                val readLater = localDataSource.readLaterNews()
                    .map { it.map { readLaterToModelMapper.invoke(it) } }
                    .flowOn(context = dispatcher)
                readLater
            }

            SavedNewsFilter.SAVED -> {
                val saved = localDataSource.savedNews().map { it.map { modelMapper.invoke(it) } }
                    .flowOn(context = dispatcher)
                saved
            }
        }
    }

    override suspend fun fetchUsBusinessNews(): List<ArticleModel> {
        page++
        val response = remoteDataSource.fetchUsBusinessNews(page)
        val news = response.articles
        val newsCount = localDataSource.newsCountByTitle(news.map { it.title })
        return if (news.size != newsCount) {
            //save locally and returns inserted items' generated ids
            cacheDataSource.saveNews(response.articles)
            val modelList: List<ArticleModel> = response.articles.map { articleMapper.invoke(it) }
            modelList
        } else {
            emptyList()
        }
    }

    override fun search(query: String): Flow<List<ArticleModel>> {
        return localDataSource.search("%".plus(query).plus("%"))
            .map { it.map { entity -> modelMapper(entity) } }.flowOn(context = dispatcher)
    }

}