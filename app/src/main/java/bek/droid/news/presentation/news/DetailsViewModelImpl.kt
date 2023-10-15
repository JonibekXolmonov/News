package bek.droid.news.presentation.news

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.util.FileUtils
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.domain.use_case.NewsUseCase
import bek.droid.news.presentation.viewModel.DetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
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
class DetailsViewModelImpl @Inject constructor(
    private val fileUtils: FileUtils,
    private val coroutineContext: CoroutineContext,
    private val useCase: NewsUseCase
) : ViewModel(),
    DetailsViewModel {

    private val _screenshotUri = Channel<Uri?>()
    val screenshotUri get() = _screenshotUri.receiveAsFlow()

    private val _news = MutableStateFlow<List<ArticleModel>>(emptyList())
    val news get() = _news

    private var _ivBookmarkState = Channel<Boolean>()
    val ivBookmarkState get() = _ivBookmarkState.receiveAsFlow()


    override fun captureScreenshot(view: View) {
        viewModelScope.launch {
            withContext(coroutineContext) {
                val screenshot = fileUtils.captureScreenshot(view)
                val uri: Uri? = saveBitmap(screenshot).await()
                _screenshotUri.send(uri)
            }
        }
    }

    override fun saveBitmap(bitmap: Bitmap): Deferred<Uri?> {
        return viewModelScope.async(context = coroutineContext) {
            val savedBitmapUri = fileUtils.getBitmapFromView(bitmap)
            savedBitmapUri
        }
    }

    override fun news() {
        viewModelScope.launch {
            useCase.dbNews().catch { e ->

            }.collectLatest { localNews ->
                if (localNews.isNotEmpty()) {
                    _news.value = localNews
                }
            }
        }
    }

    override fun bookmarkState(saved: Boolean?) {
        viewModelScope.launch {
            _ivBookmarkState.send(saved ?: false)
        }
    }
}