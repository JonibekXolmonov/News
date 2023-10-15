package bek.droid.news.data.mapper

import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Source
import bek.droid.news.data.model.ui_model.ArticleModel

class ImportantToModelMapper : SingleMapper<ImportantNewsEntity, ArticleModel> {
    override fun invoke(value: ImportantNewsEntity): ArticleModel = ArticleModel(
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