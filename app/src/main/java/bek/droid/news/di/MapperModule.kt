package bek.droid.news.di

import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.mapper.EntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

//    @Binds
//    fun bindSingleMapper(mapper: ArticleMapper): SingleMapper<Article, ArticleModel>

    @Provides
    fun provideArticleMapper(): ArticleMapper = ArticleMapper()

    @Provides
    fun provideEntityMapper(): EntityMapper = EntityMapper()
}