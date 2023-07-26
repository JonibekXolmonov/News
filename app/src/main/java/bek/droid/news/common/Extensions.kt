package bek.droid.news.common

import android.widget.ImageView
import bek.droid.news.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

fun ImageView.load(url: String?) {
    Picasso.get().load(url).placeholder(R.drawable.placeholder).into(this)
}


fun String.convertToDate(): String {
    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sourceSdf.parse(this)?.let { requiredSdf.format(it) } ?: ""
}