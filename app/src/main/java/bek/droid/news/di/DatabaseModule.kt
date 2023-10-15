package bek.droid.news.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import bek.droid.news.data.db.AppDatabase
import bek.droid.news.data.db.dao.ImportantNewsDao
import bek.droid.news.data.db.dao.LaterReadNewsDao
import bek.droid.news.data.db.dao.NewsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsRoomDB(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "news.db"
        ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.createNewsDao()
    }

    @Provides
    @Singleton
    fun provideImportantNewsDao(appDatabase: AppDatabase): ImportantNewsDao {
        return appDatabase.createImportantNewsDoa()
    }


    @Provides
    @Singleton
    fun provideLaterReadNewsDao(appDatabase: AppDatabase): LaterReadNewsDao {
        return appDatabase.createLaterReadNewsDoa()
    }
}