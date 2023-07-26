package bek.droid.news.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bek.droid.news.data.model.entity.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun createTaskDao(): NewsDao
}