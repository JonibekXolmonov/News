package bek.droid.news.data.db

import androidx.room.*
import bek.droid.news.data.model.entity.NewsEntity
import kotlinx.coroutines.flow.*

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(news: List<NewsEntity>): List<Long>

    @Query("SELECT * FROM news")
    fun getNews(): Flow<List<NewsEntity>>
}