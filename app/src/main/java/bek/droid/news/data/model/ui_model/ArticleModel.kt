package bek.droid.news.data.model.ui_model

import bek.droid.news.data.model.response.Source

data class ArticleModel(
    val source: Source,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)