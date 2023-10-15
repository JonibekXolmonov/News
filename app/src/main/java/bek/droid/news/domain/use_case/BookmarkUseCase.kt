package bek.droid.news.domain.use_case

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.data.model.ui_model.ArticleModel

interface BookmarkUseCase {
    suspend fun addToBookmark(
        articleModel: ArticleModel
    ): Result<Boolean>

    suspend fun removeFromBookmark(articleModel: ArticleModel): Result<Boolean>

    suspend fun existInReadLater(id:Int): Boolean

    suspend fun existInImportant(id: Int): Boolean

    suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState

    suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState
}