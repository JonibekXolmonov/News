package bek.droid.news.data.use_case

import bek.droid.news.common.enums.CollectionState
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.repository.MainRepository
import bek.droid.news.domain.use_case.BookmarkUseCase
import javax.inject.Inject

class BookmarkUseCaseImpl @Inject constructor(private val mainRepository: MainRepository) :
    BookmarkUseCase {

    override suspend fun addToBookmark(articleModel: ArticleModel): Result<Boolean> {
        return mainRepository.addToBookmark(articleModel)
    }

    override suspend fun removeFromBookmark(articleModel: ArticleModel): Result<Boolean> {
        return mainRepository.removeFromBookmark(articleModel)
    }

    override suspend fun existInReadLater(id: Int): Boolean {
        return mainRepository.existInLaterRead(id)
    }

    override suspend fun existInImportant(id: Int): Boolean {
        return mainRepository.existInImportant(id)
    }

    override suspend fun saveToReadLaterCollection(newsModel: ArticleModel): CollectionState {
        return mainRepository.saveToReadLaterCollection(newsModel)
    }

    override suspend fun saveToImportantCollection(newsModel: ArticleModel): CollectionState {
        return mainRepository.saveToImportantCollection(newsModel)
    }
}