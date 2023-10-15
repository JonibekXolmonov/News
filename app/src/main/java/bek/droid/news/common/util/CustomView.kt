package bek.droid.news.common.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import bek.droid.news.R

class BookmarkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) :
    LinearLayout(context, attrs, defStyle, defStyleRes) {


    var ivMain: ImageView
    var tvType: TextView
    var tvNewsCount: TextView
    var icBookmark: ImageView
    var mainImage: Drawable? = null
        set(value) {
            field = value
            value?.let {
                ivMain.setImageDrawable(mainImage)
            }
        }

    var bookmarkName: String = ""
        set(value) {
            field = value
            value.let {
                tvType.text = bookmarkName
            }
        }

    var newsCount: String = ""
        set(value) {
            field = value
            value.let {
                tvNewsCount.text = newsCount
            }
        }

    var bookmarkIcon: Drawable? = null
        set(value) {
            field = value
            value?.let {
                icBookmark.setImageDrawable(bookmarkIcon)
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.bookmark_layout, this, true)
        orientation = VERTICAL

        ivMain = findViewById(R.id.ivMainImage)
        tvType = findViewById(R.id.tvBookmarkType)
        tvNewsCount = findViewById(R.id.tvNewsCount)
        icBookmark = findViewById(R.id.ivBookmark)

        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.BookmarkView)

            val mainImageRes = typedArray.getResourceId(R.styleable.BookmarkView_mainImage, -1)
            if (mainImageRes != -1) {
                mainImage = AppCompatResources.getDrawable(context, mainImageRes)
            }

            val bookmarkIconRes = typedArray.getResourceId(
                R.styleable.BookmarkView_bookmarkIcon,
                R.drawable.ic_bookmark
            )
            if (mainImageRes != R.drawable.ic_bookmark) {
                bookmarkIcon = AppCompatResources.getDrawable(context, bookmarkIconRes)
            }

            bookmarkName = resources.getText(
                typedArray.getResourceId(
                    R.styleable.BookmarkView_bookmarkName,
                    R.string.us_business_news
                )
            ).toString()

            newsCount = resources.getText(
                typedArray.getResourceId(
                    R.styleable.BookmarkView_newsCount,
                    R.string.str_count
                )
            ).toString()

            typedArray.recycle()
        }
    }
}