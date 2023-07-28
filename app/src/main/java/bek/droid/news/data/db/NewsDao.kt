package bek.droid.news.data.db

import androidx.room.*
import bek.droid.news.data.model.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<NewsEntity>)
}