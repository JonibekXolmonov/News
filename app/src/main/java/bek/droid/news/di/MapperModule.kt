package bek.droid.news.di

import android.view.Display.Mode
import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.mapper.EntityMapper
import bek.droid.news.data.mapper.ModelMapper
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


    @Provides
    fun provideModelMapper(): ModelMapper = ModelMapper()
}