package bek.droid.news.data.source

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.db.dao.ImportantNewsDao
import bek.droid.news.data.db.dao.LaterReadNewsDao
import bek.droid.news.data.db.dao.NewsDao
import bek.droid.news.data.mapper.EntityMapper
import bek.droid.news.data.mapper.ImportantToModelMapper
import bek.droid.news.data.mapper.ModelMapper
import bek.droid.news.data.mapper.ModelToEntityMapper
import bek.droid.news.data.mapper.ModelToImportantMapper
import bek.droid.news.data.mapper.ModelToReadLaterMapper
import bek.droid.news.data.mapper.ReadLaterToModelMapper
import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.datasource.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val laterReadNewsDao: LaterReadNewsDao,
    private val importantNewsDao: ImportantNewsDao,
    private val readLaterMapper: ModelToReadLaterMapper,
    private val importantMapper: ModelToImportantMapper,
) : LocalDataSource {

    override fun getNews(): Flow<List<NewsEntity>> {
        return newsDao.getNews()
    }

    override fun search(query: String): Flow<List<NewsEntity>> {
        return newsDao.search(query)
    }

    override suspend fun addToBookmark(newsModel: ArticleModel) {
        newsDao.bookmark(newsModel.id!!, true)
    }

    override suspend fun removeFromBookmark(newsModel: ArticleModel) {
        newsDao.bookmark(newsModel.id!!, false)
    }

    override suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState {
        val entity = readLaterMapper.invoke(newsModel)
        return laterReadNewsDao.insertOrRemoveIfExists(entity)
    }

    override suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState {
        val entity = importantMapper.invoke(newsModel)
        return importantNewsDao.insertOrRemoveIfExists(entity)
    }

    override suspend fun newsCountByTitle(titles: List<String?>): Int {
        return newsDao.getNewsCountByTitle(titles)
    }

    override suspend fun countFromReadLater(id: Int): Int {
        return laterReadNewsDao.count(id)
    }

    override suspend fun countFromImportant(id: Int): Int {
        return importantNewsDao.count(id)
    }

    override fun savedNews(): Flow<List<NewsEntity>> {
        return newsDao.getSavedNews()
    }

    override fun importantNews(): Flow<List<ImportantNewsEntity>> {
        return importantNewsDao.getNews()
    }

    override fun readLaterNews(): Flow<List<ReadLaterNewsEntity>> {
        return laterReadNewsDao.getNews()
    }
}