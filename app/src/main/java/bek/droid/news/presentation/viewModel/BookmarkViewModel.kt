package bek.droid.news.presentation.viewModel

import bek.droid.news.data.model.ui_model.ArticleModel

interface BookmarkViewModel {
    fun controlSaving(articleModel: ArticleModel)

    fun saveNews(articleModel: ArticleModel)

    fun removeFromSaved(articleModel: ArticleModel)

    fun isSavedInReadLater(id: Int)

    fun isSavedInImportant(id: Int)

    fun removeIfExitsElseAddToReadLater(articleModel: ArticleModel)

    fun removeIfExitsElseAddToImportant(articleModel: ArticleModel)

}