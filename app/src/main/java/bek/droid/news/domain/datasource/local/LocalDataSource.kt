package bek.droid.news.domain.datasource.local

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNews(): Flow<List<NewsEntity>>

    fun search(query: String): Flow<List<NewsEntity>>

    suspend fun addToBookmark(newsModel: ArticleModel)

    suspend fun removeFromBookmark(newsModel: ArticleModel)

    suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState

    suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState

    suspend fun newsCountByTitle(titles: List<String?>): Int

    suspend fun countFromReadLater(id: Int): Int

    suspend fun countFromImportant(id: Int): Int

    fun savedNews(): Flow<List<NewsEntity>>
    fun importantNews(): Flow<List<ImportantNewsEntity>>
    fun readLaterNews(): Flow<List<ReadLaterNewsEntity>>
}