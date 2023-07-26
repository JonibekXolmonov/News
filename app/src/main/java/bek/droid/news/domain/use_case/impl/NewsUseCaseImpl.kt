package bek.droid.news.domain.use_case.impl

import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.repository.MainRepository
import bek.droid.news.domain.use_case.NewsUseCase
import java.lang.Exception
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(private val mainRepository: MainRepository) :
    NewsUseCase {
    override suspend fun invoke(): Result<List<ArticleModel>> {
        return try {
            val response = mainRepository.fetchUsBusinessNews()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}