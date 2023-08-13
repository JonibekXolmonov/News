package bek.droid.news.presentation.news

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bek.droid.news.common.util.FileUtils
import bek.droid.news.presentation.viewModel.DetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DetailsViewModelImpl @Inject constructor(
    private val fileUtils: FileUtils,
    private val coroutineContext: CoroutineContext
) : ViewModel(),
    DetailsViewModel {

    private val _screenshotUri = MutableStateFlow<Uri?>(null)
    val screenshotUri get() = _screenshotUri

    override fun captureScreenshot(view: View) {
        viewModelScope.launch {
            withContext(coroutineContext) {
                val screenshot = fileUtils.captureScreenshot(view)
                val uri: Uri? = saveBitmap(screenshot).await()
                _screenshotUri.value = uri
            }
        }
    }

    override fun saveBitmap(bitmap: Bitmap): Deferred<Uri?> {
        return viewModelScope.async(context = coroutineContext) {
            val savedBitmapUri = fileUtils.getBitmapFromView(bitmap)
            savedBitmapUri
        }
    }
}