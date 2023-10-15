package bek.droid.news.di

import android.view.Display.Mode
import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.mapper.EntityMapper
import bek.droid.news.data.mapper.ImportantToModelMapper
import bek.droid.news.data.mapper.ModelMapper
import bek.droid.news.data.mapper.ModelToEntityMapper
import bek.droid.news.data.mapper.ModelToImportantMapper
import bek.droid.news.data.mapper.ModelToReadLaterMapper
import bek.droid.news.data.mapper.ReadLaterToModelMapper
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

    @Provides
    fun provideModelToEntityMapper(): ModelToEntityMapper = ModelToEntityMapper()

    @Provides
    fun provideModelToReadLaterEntityMapper(): ModelToReadLaterMapper = ModelToReadLaterMapper()

    @Provides
    fun provideModelToImportantEntityMapper(): ModelToImportantMapper = ModelToImportantMapper()

    @Provides
    fun provideImportantToModel(): ImportantToModelMapper = ImportantToModelMapper()


    @Provides
    fun provideLaterReadToModel(): ReadLaterToModelMapper = ReadLaterToModelMapper()
}