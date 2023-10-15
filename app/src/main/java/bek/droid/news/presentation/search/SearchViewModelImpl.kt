package bek.droid.news.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.enums.UiStateList
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.use_case.BookmarkUseCase
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchViewModelImpl @Inject constructor(
    private val newsUseCase: NewsUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val dispatcher: CoroutineContext
) : SearchViewModel,
    ViewModel() {

    private val _newsState = MutableStateFlow<UiStateList<ArticleModel>>(UiStateList.EMPTY)
    val newsState get() = _newsState.asStateFlow()

    override fun search(query: String) {
        if (query.isBlank()) {
            _newsState.value = UiStateList.SUCCESS(emptyList<ArticleModel>().toList())
            return
        }

        if (query.length >= 2) {
            _newsState.value = UiStateList.LOADING
            viewModelScope.launch {
                newsUseCase.search(query)
                    .catch {
                        _newsState.value =
                            UiStateList.ERROR(it.localizedMessage ?: "Something went wrong!")
                    }
                    .collectLatest {
                        _newsState.value = UiStateList.SUCCESS(it)
                    }
            }
        }
    }
}