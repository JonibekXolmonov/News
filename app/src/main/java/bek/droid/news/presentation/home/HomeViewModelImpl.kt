package bek.droid.news.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.UiStateList
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val newsUseCase: NewsUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel(), HomeViewModel {

    private val _newsState = MutableStateFlow<UiStateList<ArticleModel>>(UiStateList.EMPTY)
    val newsState get() = _newsState.asStateFlow()

    override fun fetchNews() {
        _newsState.value = UiStateList.LOADING
        viewModelScope.launch {
            withContext(dispatcher) {
                val result = newsUseCase.invoke()

                result.onSuccess { news ->
                    Log.d("TAG", "fetchNews: $news")
                    _newsState.value = UiStateList.SUCCESS(news)
                }
                result.onFailure {
                    Log.d("TAG", "fetchNews: ${it.localizedMessage}")
                    _newsState.value = UiStateList.ERROR(
                        message = it.message ?: it.localizedMessage ?: "Something went wrong"
                    )
                }
            }
        }
    }
}