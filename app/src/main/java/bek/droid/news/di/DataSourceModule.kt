package bek.droid.news.di

import bek.droid.news.data.db.NewsDao
import bek.droid.news.data.network.ApiService
import bek.droid.news.data.source.CacheDataSourceImpl
import bek.droid.news.data.source.LocalDataSourceImpl
import bek.droid.news.data.source.RemoteDataSourceImpl
import bek.droid.news.domain.datasource.cache.CacheDataSource
import bek.droid.news.domain.datasource.local.LocalDataSource
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideLocalDataSource(
        newsDao: NewsDao
    ): LocalDataSource = LocalDataSourceImpl(newsDao = newsDao)

    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService
    ): RemoteDataSource = RemoteDataSourceImpl(apiService = apiService)

    @Provides
    fun provideCacheDataSource(
        newsDao: NewsDao
    ): CacheDataSource = CacheDataSourceImpl(newsDao = newsDao)

}