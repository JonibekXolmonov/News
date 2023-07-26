package bek.droid.news.di

import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.domain.use_case.impl.NewsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindLoginUseCase(useCase: NewsUseCaseImpl): NewsUseCase

}