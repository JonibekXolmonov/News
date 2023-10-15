package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Source
import bek.droid.news.data.model.ui_model.ArticleModel

class ModelToEntityMapper : SingleMapper<ArticleModel, NewsEntity> {
    override fun invoke(value: ArticleModel): NewsEntity = NewsEntity(
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