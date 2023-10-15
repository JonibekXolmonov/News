package bek.droid.news.data.use_case

import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.repository.MainRepository
import bek.droid.news.domain.use_case.NewsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(private val mainRepository: MainRepository) :
    NewsUseCase {

    override fun dbNews(): Flow<List<ArticleModel>> {
        return mainRepository.getDbNews()
    }

    override fun savedNews(filter: SavedNewsFilter): Flow<List<ArticleModel>> {
        return mainRepository.savedNews(filter)
    }

    override suspend fun invoke(): Result<List<ArticleModel>> {
        return try {
            val response = mainRepository.fetchUsBusinessNews()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun search(query: String): Flow<List<ArticleModel>> {
        return mainRepository.search(query)
    }
}