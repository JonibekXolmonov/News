package bek.droid.news.di

import bek.droid.news.data.mapper.ArticleMapper
import bek.droid.news.data.mapper.ModelMapper
import bek.droid.news.data.repository.MainRepositoryImpl
import bek.droid.news.domain.datasource.cache.CacheDataSource
import bek.droid.news.domain.datasource.local.LocalDataSource
import bek.droid.news.domain.datasource.remote.RemoteDataSource
import bek.droid.news.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMainRepository(
        localDataSource: LocalDataSource,
        cacheDataSource: CacheDataSource,
        remoteDataSource: RemoteDataSource,
        mapper: ArticleMapper,
        dispatcher: CoroutineContext,
        modelMapper: ModelMapper
    ): MainRepository =
        MainRepositoryImpl(
            localDataSource,
            cacheDataSource,
            remoteDataSource,
            mapper,
            dispatcher,
            modelMapper
        )
}