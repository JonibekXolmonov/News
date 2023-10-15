package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Source
import bek.droid.news.data.model.ui_model.ArticleModel

class ModelMapper : SingleMapper<NewsEntity, ArticleModel> {
    override fun invoke(value: NewsEntity): ArticleModel = ArticleModel(
        id = value.id,
        source = Source(id = value.sourceId, name = value.sourceName),
        author = value.author,
        title = value.title,
        description = value.description,
        url = value.url,
        urlToImage = value.urlToImage,
        publishedAt = value.publishedAt,
        content = value.content,
        isSaved = value.isSaved
    )
}