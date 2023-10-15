package bek.droid.news.domain.repository

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun fetchUsBusinessNews(): List<ArticleModel>

    fun search(query: String): Flow<List<ArticleModel>>

    fun getDbNews(): Flow<List<ArticleModel>>

    suspend fun addToBookmark(articleModel: ArticleModel): Result<Boolean>

    suspend fun removeFromBookmark(articleModel: ArticleModel): Result<Boolean>

    suspend fun existInLaterRead(id: Int): Boolean

    suspend fun existInImportant(id: Int): Boolean

    suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState

    suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState

    fun savedNews(filter: SavedNewsFilter): Flow<List<ArticleModel>>
}