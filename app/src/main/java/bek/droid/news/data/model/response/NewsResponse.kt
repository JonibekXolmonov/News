package bek.droid.news.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

typealias NewsResponse = BaseResponse<Article>

data class Article(
    val source: Source,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)

@Parcelize
data class Source(
    val id: String? = null,
    val name: String? = null
) : Parcelable