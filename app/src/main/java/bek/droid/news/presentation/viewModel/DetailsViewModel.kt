package bek.droid.news.presentation.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import kotlinx.coroutines.Deferred

interface DetailsViewModel {

    fun captureScreenshot(view: View)

    fun saveBitmap(bitmap: Bitmap): Deferred<Uri?>

    fun news()

    fun bookmarkState(saved: Boolean?)
}