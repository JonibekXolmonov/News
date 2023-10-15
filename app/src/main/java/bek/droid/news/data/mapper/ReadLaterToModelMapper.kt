package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import bek.droid.news.data.model.response.Source
import bek.droid.news.data.model.ui_model.ArticleModel

class ReadLaterToModelMapper : SingleMapper<ReadLaterNewsEntity, ArticleModel> {
    override fun invoke(value: ReadLaterNewsEntity): ArticleModel = ArticleModel(
        id = value.id,
        source = Source(value.sourceId, value.sourceName),
        author = value.author,
        title = value.title,
        description = value.description,
        url = value.url,
        urlToImage = value.urlToImage,
        publishedAt = value.publishedAt,
        content = value.content
    )
}