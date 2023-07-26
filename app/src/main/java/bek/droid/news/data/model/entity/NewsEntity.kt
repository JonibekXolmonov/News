package bek.droid.news.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TABLE_NAME")
data class NewsEntity(

    @PrimaryKey val taskID: Long,
    val index: Long,
    val projectID: String,
    val projectName: String,
    val ownerID: Long,
    val ownerName: String,
    val ownerAvatar: String,
    val executorID: Long,
    val executorName: String,
    val executorAvatar: String,
    val taskDate: String,
    val termDate: String,
    val name: String,
    val priority: String,
    val status: String
)
