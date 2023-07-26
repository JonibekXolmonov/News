package bek.droid.news.di

import bek.droid.news.common.SingleMapper
import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.model.response.Article
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.data.repository.MainRepositoryImpl
import bek.droid.news.domain.datasource.cache.CacheDataSource
import bek.droid.news.domain.datasource.local.LocalDataSource
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import bek.droid.news.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMainRepository(
        localDataSource: LocalDataSource,
        cacheDataSource: CacheDataSource,
        remoteDataSource: RemoteDataSource,
        mapper: ArticleMapper
    ): MainRepository =
        MainRepositoryImpl(localDataSource, cacheDataSource, remoteDataSource, mapper)
}