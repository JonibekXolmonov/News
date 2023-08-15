package bek.droid.news.domain.use_case

import bek.droid.news.data.model.ui_model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    suspend fun invoke(): Result<List<ArticleModel>>

     fun search(query: String):Flow<List<ArticleModel>>

    val allDbNews: Flow<List<ArticleModel>>

}