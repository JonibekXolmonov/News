package bek.droid.news.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import bek.droid.news.common.Constants
import bek.droid.news.common.enums.CollectionState
import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImportantNewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(news: ImportantNewsEntity): Long

    @Delete
    suspend fun remove(importantNewsEntity: ImportantNewsEntity)

    @Query("SELECT * FROM important_news ORDER BY id DESC")
    fun getNews(): Flow<List<ImportantNewsEntity>>

    @Query("SELECT COUNT() FROM important_news WHERE id = :id")
    suspend fun count(id: Int): Int

    @Transaction
    suspend fun insertOrRemoveIfExists(entity: ImportantNewsEntity): CollectionState {
        val id = insert(entity)
        return if (id == -1L) {
            remove(entity)
            CollectionState.REMOVED
        } else {
            CollectionState.INSERTED
        }
    }
}