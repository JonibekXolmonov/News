package bek.droid.news.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.enums.CollectionState
import bek.droid.news.data.db.dao.LaterReadNewsDao
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.use_case.BookmarkUseCase
import bek.droid.news.presentation.viewModel.BookmarkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class BookmarkViewModelImpl @Inject constructor(
    private val dispatcher: CoroutineContext,
    private val bookmarkUseCase: BookmarkUseCase,
) : ViewModel(), BookmarkViewModel {

    private val _isInReadLater = Channel<Boolean>()
    val isInReadLater get() = _isInReadLater.receiveAsFlow()

    private val _isInImportant = Channel<Boolean>()
    val isInImportant get() = _isInImportant.receiveAsFlow()

    private val _isSaved = Channel<Boolean>()
    val isSaved get() = _isSaved.receiveAsFlow()

    override fun controlSaving(articleModel: ArticleModel) {
        if (articleModel.id != null) {
            if (articleModel.isSaved) {
                removeFromSaved(articleModel)
            } else {
                saveNews(articleModel)
            }
        }
    }

    override fun saveNews(articleModel: ArticleModel) {
        viewModelScope.launch(dispatcher) {
            //correction later
            bookmarkUseCase.addToBookmark(articleModel)
                .onSuccess {
                    _isSaved.send(it)
                }
                .onFailure {

                }
        }
    }

    override fun removeFromSaved(articleModel: ArticleModel) {
        viewModelScope.launch(dispatcher) {
            bookmarkUseCase.removeFromBookmark(articleModel)
                .onSuccess {
                    _isSaved.send(!it)
                }
                .onFailure {

                }
        }
    }

    override fun isSavedInReadLater(id: Int) {
        viewModelScope.launch(dispatcher) {
            val result = bookmarkUseCase.existInReadLater(id)
            _isInReadLater.send(result)
        }
    }

    override fun isSavedInImportant(id: Int) {
        viewModelScope.launch(dispatcher) {
            val result = bookmarkUseCase.existInImportant(id)
            _isInImportant.send(result)
        }
    }

    override fun removeIfExitsElseAddToReadLater(articleModel: ArticleModel) {
        viewModelScope.launch {
            val updateResult = bookmarkUseCase.saveToReadLaterCollection(articleModel)
            _isInReadLater.send(updateResult == CollectionState.INSERTED)
        }
    }

    override fun removeIfExitsElseAddToImportant(articleModel: ArticleModel) {
        viewModelScope.launch {
            val updateResult = bookmarkUseCase.saveToImportantCollection(articleModel)
            _isInImportant.send(updateResult == CollectionState.INSERTED)
        }
    }
}