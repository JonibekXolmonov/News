package bek.droid.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.enums.UiStateList
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.data.model.ui_model.NewAvailable
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val newsUseCase: NewsUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel(), HomeViewModel {

    private val _newsState = Channel<UiStateList<ArticleModel>>()
    val newsState get() = _newsState.receiveAsFlow()

    private val _newNewsAvailable = MutableStateFlow(NewAvailable())
    val newNewsAvailable get() = _newNewsAvailable

    override fun news() {
        loading()

        viewModelScope.launch {
            newsUseCase.dbNews().catch { e ->
                _newsState.send(
                    UiStateList.ERROR(
                        message = e.message ?: e.localizedMessage
                        ?: "Something went wrong with Local"
                    )
                )
            }.collectLatest { localNews ->
                if (localNews.isNotEmpty()) {
                    updateNews(localNews)
                }
            }
        }
    }

    override fun fetchNews() {
        viewModelScope.launch {
            withContext(dispatcher) {
                val result = newsUseCase.invoke()

                result.onSuccess { fetchedNews ->
                    if (fetchedNews.isNotEmpty()) {
                        updateNewNewsState(
                            isAvailable = true,
                            list = fetchedNews
                        )
                    }
                }
                result.onFailure {
                    _newsState.send(
                        UiStateList.ERROR(
                            message = it.message ?: it.localizedMessage ?: "Something went wrong"
                        )
                    )
                }
            }
        }
    }


    override fun loading() {
        viewModelScope.launch {
            _newsState.send(UiStateList.LOADING)
        }
    }

    override fun updateNewNewsState(isAvailable: Boolean, list: List<ArticleModel>) {
        _newNewsAvailable.value = NewAvailable(isAvailable, list)
    }

    override fun updateNews(news: List<ArticleModel>) {
        viewModelScope.launch {
            _newsState.send(UiStateList.SUCCESS(news.toList()))
        }
    }
}