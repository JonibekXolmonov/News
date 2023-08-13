package bek.droid.news.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FileUtils @Inject constructor(@ApplicationContext val context: Context) {
    fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.externalCacheDir,
                System.currentTimeMillis().toString() + ".jpg"
            )

            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun captureScreenshot(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background

        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }
}