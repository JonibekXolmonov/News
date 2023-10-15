package bek.droid.news.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.common.enums.UiStateList
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.SavedNewsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SavedNewsViewModelImpl @Inject constructor(
    private val newsUseCase: NewsUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel(), SavedNewsViewModel {

    private var _news = MutableStateFlow<UiStateList<ArticleModel>>(UiStateList.EMPTY)
    val savedNews get() = _news.asStateFlow()

    override fun savedNews(filter: SavedNewsFilter) {
        viewModelScope.launch(dispatcher) {

            _news.value = (UiStateList.LOADING)

            newsUseCase.savedNews(filter).catch {
                _news.value = (UiStateList.ERROR(it.localizedMessage))
            }.collectLatest {
                _news.value = (UiStateList.SUCCESS(it))
            }
        }
    }
}