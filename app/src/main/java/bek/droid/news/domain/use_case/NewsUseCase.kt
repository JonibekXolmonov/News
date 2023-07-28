package bek.droid.news.domain.use_case

import bek.droid.news.data.model.ui_model.ArticleModel

interface NewsUseCase {
    suspend fun invoke(): Result<List<ArticleModel>>

}