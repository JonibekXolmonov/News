package bek.droid.news.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bek.droid.news.data.db.dao.ImportantNewsDao
import bek.droid.news.data.db.dao.LaterReadNewsDao
import bek.droid.news.data.db.dao.NewsDao
import bek.droid.news.data.model.entity.ImportantNewsEntity
import bek.droid.news.data.model.entity.NewsEntity
import bek.droid.news.data.model.entity.ReadLaterNewsEntity

@Database(
    entities = [NewsEntity::class, ImportantNewsEntity::class, ReadLaterNewsEntity::class],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun createNewsDao(): NewsDao

    abstract fun createImportantNewsDoa(): ImportantNewsDao

    abstract fun createLaterReadNewsDoa(): LaterReadNewsDao
}