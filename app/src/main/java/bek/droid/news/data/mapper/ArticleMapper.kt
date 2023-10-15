package bek.droid.news.data.mapper

import bek.droid.news.data.model.response.Article
import bek.droid.news.data.model.ui_model.ArticleModel

class ArticleMapper : SingleMapper<Article, ArticleModel> {

    override fun invoke(value: Article): ArticleModel = ArticleModel(
        source = value.source,
        author = value.author,
        title = value.title,
        description = value.description,
        url = value.url,
        urlToImage = value.urlToImage,
        publishedAt = value.publishedAt,
        content = value.content
    )
}