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

    @Query("SELECT * FROM news WHERE (source_name LIKE :query or title LIKE :query or description LIKE :query or content LIKE :query)")
    fun search(query: String): Flow<List<NewsEntity>>
}