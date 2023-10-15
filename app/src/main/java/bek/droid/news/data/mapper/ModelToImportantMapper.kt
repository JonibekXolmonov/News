package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Source
import bek.droid.news.data.model.ui_model.ArticleModel

class ModelToImportantMapper : SingleMapper<ArticleModel, ImportantNewsEntity> {
    override fun invoke(value: ArticleModel): ImportantNewsEntity = ImportantNewsEntity(
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