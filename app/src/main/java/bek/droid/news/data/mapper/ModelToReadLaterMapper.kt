package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import bek.droid.news.data.model.ui_model.ArticleModel

class ModelToReadLaterMapper : SingleMapper<ArticleModel, ReadLaterNewsEntity> {
    override fun invoke(value: ArticleModel): ReadLaterNewsEntity = ReadLaterNewsEntity(
        id = value.id,
        sourceId = value.source?.id,
        sourceName = value.source?.name,
        author = value.author,
        title = value.title,
        description = value.description,
        url = value.url,
        urlToImage = value.urlToImage,
        publishedAt = value.publishedAt,
        content = value.content
    )
}