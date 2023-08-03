package bek.droid.news.data.model.ui_model

data class NewAvailable(
    val isAvailable: Boolean = false,
    val news: List<ArticleModel> = emptyList()
)