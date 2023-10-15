package bek.droid.news.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import bek.droid.news.common.enums.CollectionState
import bek.droid.news.data.model.entity.ReadLaterNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaterReadNewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(news: ReadLaterNewsEntity): Long

    @Delete
    suspend fun remove(newsEntity: ReadLaterNewsEntity)

    @Query("SELECT * FROM read_later_news ORDER BY id DESC")
    fun getNews(): Flow<List<ReadLaterNewsEntity>>

    @Query("SELECT COUNT() FROM read_later_news WHERE id = :id")
    suspend fun count(id: Int): Int

    @Transaction
    suspend fun insertOrRemoveIfExists(entity: ReadLaterNewsEntity): CollectionState {
        val id = insert(entity)
        return if (id == -1L) {
            remove(entity)
            CollectionState.REMOVED
        } else {
            CollectionState.INSERTED
        }
    }
}