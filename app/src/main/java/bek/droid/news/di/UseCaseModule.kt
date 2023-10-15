package bek.droid.news.di

import bek.droid.news.data.use_case.BookmarkUseCaseImpl
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.data.use_case.NewsUseCaseImpl
import bek.droid.news.domain.use_case.BookmarkUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindLoginNewsUseCase(useCase: NewsUseCaseImpl): NewsUseCase

    @Binds
    fun bindLoginBookmarkUseCase(useCase: BookmarkUseCaseImpl): BookmarkUseCase

}