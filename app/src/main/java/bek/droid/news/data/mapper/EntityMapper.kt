package bek.droid.news.data.mapper

import bek.droid.news.common.SingleMapper
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.response.Article

class EntityMapper : SingleMapper<Article, NewsEntity> {
    override fun invoke(value: Article): NewsEntity = NewsEntity(
        sourceId = value.source.id,
        sourceName = value.source.name,
        author = value.author,
        title = value.title,
        description = value.description,
        url = value.url,
        urlToImage = value.urlToImage,
        publishedAt = value.publishedAt,
        content = value.content
    )
}