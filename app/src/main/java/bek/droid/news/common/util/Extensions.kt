package bek.droid.news.common.util

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import bek.droid.news.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.drawee.view.SimpleDraweeView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun ImageView.loadWithLoadingThumb(url: String?) {
    Glide.with(context).load(url)
        .thumbnail(Glide.with(this).load(R.drawable.loading_gif))
        .error(R.drawable.logo)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun SimpleDraweeView.loadWithFresco(url: String?) {
    if (!url.isNullOrEmpty()) {
        setImageURI(url)
    } else {
        setActualImageResource(R.drawable.logo_white)
    }
}

fun String.formatDate(): String {
    val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    try {
        val apiDateTime = apiDateFormat.parse(this)
        if (apiDateTime != null) {
            val currentTime = Calendar.getInstance().time
            val diffMillis = currentTime.time - apiDateTime.time

            val diffSeconds = diffMillis / 1000
            val diffMinutes = diffSeconds / 60
            val diffHours = diffMinutes / 60
            val diffDays = diffHours / 24

            return when {
                diffDays > 1 -> "$diffDays days ago"
                diffDays == 1L -> "yesterday"
                diffHours > 0 -> "$diffHours hours ago"
                diffMinutes > 0 -> "$diffMinutes mins ago"
                else -> "just now"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return "Unknown"
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun AppCompatEditText.isNotEmpty() = text?.isNotEmpty() ?: true

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun Fragment.showMessage(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun View.fadeVisibility(visibility: Int, duration: Long = 500) {
    val transition = Fade()
    transition.duration = duration
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.visibility = visibility
}

fun <T> isEqual(first: List<T>, second: List<T>): Boolean {

    if (first.size != second.size) {
        return false
    }

    return first.zip(second).all { (x, y) -> x?.equals(y) ?: false }
}

fun <T> getMinusList(first: List<T>, second: List<T>) = first.minus(second.toSet())

fun String?.isNotNull() = this != "NULL"

fun Fragment.openNewsInBrowser(url: String?) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}

fun Fragment.shareLinkAsText(url: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/html";
    intent.putExtra(Intent.EXTRA_TEXT, url)

    startActivity(Intent.createChooser(intent, "Share news"));
}

fun Fragment.shareScreenShot(bitmapUri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    startActivity(Intent.createChooser(intent, "Share news screenshot"))
}

fun TextView.setSpannableText(originalText: String?, onCLick: () -> Unit) {
    val additionalText = " read on web"

    val spannableString = SpannableString(originalText + additionalText)

    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onCLick()
        }
    }

    spannableString.setSpan(
        clickableSpan,
        originalText?.length ?: 0,
        spannableString.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        ForegroundColorSpan(Color.WHITE),
        spannableString.length - additionalText.length,
        spannableString.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        RelativeSizeSpan(0.8f),
        spannableString.length - additionalText.length,
        spannableString.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )


    text = spannableString

    movementMethod = LinkMovementMethod.getInstance()
}

