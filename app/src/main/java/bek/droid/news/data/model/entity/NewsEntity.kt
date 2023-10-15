package bek.droid.news.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "news",
    indices = [Index(
        value = ["title", "url"],
        unique = true
    )]
)
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "source_id")
    val sourceId: String?,
    @ColumnInfo(name = "source_name")
    val sourceName: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "url_to_image")
    val urlToImage: String?,
    @ColumnInfo(name = "published_at")
    val publishedAt: String?,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean = false
)
