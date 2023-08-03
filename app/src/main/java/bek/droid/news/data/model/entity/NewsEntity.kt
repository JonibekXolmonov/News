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
    val sourceId: String? = null,
    @ColumnInfo(name = "source_name")
    val sourceName: String? = null,
    @ColumnInfo(name = "author")
    val author: String? = null,
    @ColumnInfo(name = "title")
    val title: String? = "",
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "url")
    val url: String? = null,
    @ColumnInfo(name = "url_to_image")
    val urlToImage: String? = null,
    @ColumnInfo(name = "published_at")
    val publishedAt: String? = null,
    @ColumnInfo(name = "content")
    val content: String? = null
)
