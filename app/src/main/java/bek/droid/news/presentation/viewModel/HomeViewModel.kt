package bek.droid.news.presentation.viewModel

import bek.droid.news.data.model.ui_model.ArticleModel

interface HomeViewModel {
    fun news()

    fun fetchNews()

    fun updateNews(news: List<ArticleModel>)

    fun loading()

    fun updateNewNewsState(isAvailable: Boolean = false, list: List<ArticleModel> = emptyList())
}