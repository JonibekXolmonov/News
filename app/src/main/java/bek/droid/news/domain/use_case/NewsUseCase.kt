package bek.droid.news.domain.use_case

import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    suspend fun invoke(): Result<List<ArticleModel>>

    fun search(query: String): Flow<List<ArticleModel>>

    fun dbNews(): Flow<List<ArticleModel>>

    fun savedNews(filter: SavedNewsFilter): Flow<List<ArticleModel>>

}