package bek.droid.news.data.model.response

class BaseResponse<T>(
    val status: String,
    val totalResults: Long,
    val articles: List<T>
)