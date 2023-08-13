package bek.droid.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.UiStateList
import bek.droid.news.common.exceptions.PagingError
import bek.droid.news.common.getMinusList
import bek.droid.news.common.isEqual
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.data.model.ui_model.NewAvailable
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val _newNewsAvailable = MutableStateFlow(NewAvailable())
    val newNewsAvailable get() = _newNewsAvailable

    private var newNewsFetchedRemotely: List<ArticleModel> = listOf()
    var newsFromLocal = mutableListOf<ArticleModel>()

    override fun news() {
        loading()

        viewModelScope.launch {
            newsUseCase.allDbNews.catch { e ->
                _newsState.value = UiStateList.ERROR(
                    message = e.message ?: e.localizedMessage
                    ?: "Something went wrong with Local"
                )
            }.collectLatest { localNews ->
                if (localNews.isNotEmpty()) {
                    if (!isEqual(newsFromLocal, localNews)) {
                        newNewsFetchedRemotely = getMinusList(localNews, newsFromLocal)

                        updateNewNewsState(
                            isAvailable = localNews.isNotEmpty() && newsFromLocal.isNotEmpty() && newNewsFetchedRemotely.isNotEmpty(),
                            list = newNewsFetchedRemotely
                        )
                        newsFromLocal.addAll(0, newNewsFetchedRemotely)
                    }
                    updateNews(newsFromLocal)
                }
            }
        }
    }

    override fun fetchNews() {
//        loading()
        viewModelScope.launch {
            withContext(dispatcher) {
                val result = newsUseCase.invoke()

                result.onSuccess { news ->
//                    updateNews(news)
                }
                result.onFailure {
                    if (it is PagingError) {
                        _newsState.value = UiStateList.PAGING_END
                    } else {
                        _newsState.value = UiStateList.ERROR(
                            message = it.message ?: it.localizedMessage ?: "Something went wrong"
                        )
                    }
                }
            }
        }
    }

    override fun loading() {
        _newsState.value = UiStateList.LOADING
    }

    override fun updateNewNewsState(isAvailable: Boolean, list: List<ArticleModel>) {
        _newNewsAvailable.value = NewAvailable(isAvailable, list)
    }

    override fun updateNews(news: List<ArticleModel>) {
        _newsState.value = UiStateList.SUCCESS(news.toList())
    }
}