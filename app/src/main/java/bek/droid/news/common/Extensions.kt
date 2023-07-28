package bek.droid.news.common

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import bek.droid.news.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun ImageView.loadWithGlide(url: String?) {
    Glide.with(context).load(url).placeholder(R.drawable.placeholder).into(this)
}


fun String.convertToDate(): String {
    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sourceSdf.parse(this)?.let { requiredSdf.format(it) } ?: ""
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

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun Fragment.showMessage(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}