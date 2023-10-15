package bek.droid.news.data.model.ui_model

import android.os.Parcelable
import bek.droid.news.data.model.response.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleModel(
    val id: Int? = null,
    val source: Source? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
    var isSaved: Boolean = false
) : Parcelable {

    override fun hashCode(): Int {
        return title.hashCode() * description.hashCode() * content.hashCode() * isSaved.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ArticleModel) {
            this.title == other.title && this.content == other.content && this.description == other.description && this.isSaved == other.isSaved
        } else false
    }
}