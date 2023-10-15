package bek.droid.news.di

import bek.droid.news.data.db.dao.ImportantNewsDao
import bek.droid.news.data.db.dao.LaterReadNewsDao
import bek.droid.news.data.db.dao.NewsDao
import bek.droid.news.data.mapper.EntityMapper
import bek.droid.news.data.mapper.ModelToImportantMapper
import bek.droid.news.data.mapper.ModelToReadLaterMapper
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

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideLocalDataSource(
        newsDao: NewsDao,
        laterReadNewsDao: LaterReadNewsDao,
        importantNewsDao: ImportantNewsDao,
        readLaterMapper: ModelToReadLaterMapper,
        importantMapper: ModelToImportantMapper
    ): LocalDataSource = LocalDataSourceImpl(
        newsDao,
        laterReadNewsDao,
        importantNewsDao,
        readLaterMapper,
        importantMapper,
    )

    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService
    ): RemoteDataSource = RemoteDataSourceImpl(apiService = apiService)

    @Provides
    fun provideCacheDataSource(
        newsDao: NewsDao,
        entityMapper: EntityMapper
    ): CacheDataSource = CacheDataSourceImpl(newsDao = newsDao, entityMapper = entityMapper)

}