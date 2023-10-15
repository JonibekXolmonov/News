package bek.droid.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import bek.droid.news.R
import bek.droid.news.common.Constants.LARGE_NEWS_VIEW
import bek.droid.news.common.Constants.SMALL_NEWS_VIEW
import bek.droid.news.common.util.formatDate
import bek.droid.news.common.util.loadWithFresco
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.ItemNewsHomeLargeItemBinding
import bek.droid.news.databinding.ItemNewsHomeLayoutBinding

class NewsMainAdapter : ListAdapter<ArticleModel, ViewHolder>(DiffUtil()) {

    lateinit var onNewsClick: (ArticleModel, ImageView, Int) -> Unit
    lateinit var onBookmarkAction: (ArticleModel) -> Unit
    lateinit var onShareAction: (ArticleModel) -> Unit


    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class SmallVH(private val binding: ItemNewsHomeLayoutBinding) :
        ViewHolder(binding.root) {

        fun bind(article: ArticleModel, position: Int) {
            with(binding) {
                ivNews.loadWithFresco(article.urlToImage)
                tvTitle.text = article.title
                tvSourceName.text = article.source?.name
                tvPublishedDate.text = article.publishedAt?.formatDate()
                ivBookmark.setImageResource(if (article.isSaved) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark)
                ivNews.transitionName = article.title

                ivBookmark.setOnClickListener {
                    onBookmarkAction(article)
                }

                ivShare.setOnClickListener {
                    onShareAction(article)
                }

                root.setOnClickListener {
                    onNewsClick(article, ivNews, position)
                }
            }
        }
    }

    inner class LargeVH(private val binding: ItemNewsHomeLargeItemBinding) :
        ViewHolder(binding.root) {
        fun bind(article: ArticleModel, position: Int) {
            with(binding) {
                ivNews.loadWithFresco(article.urlToImage)
                tvTitle.text = article.title
                tvSourceName.text = article.source?.name
                tvPublishedDate.text = article.publishedAt?.formatDate()
                ivBookmark.setImageResource(if (article.isSaved) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark)
                ivNews.transitionName = article.title

                ivBookmark.setOnClickListener {
                    onBookmarkAction(article)
                }

                ivShare.setOnClickListener {
                    onShareAction(article)
                }

                root.setOnClickListener {
                    onNewsClick(article, ivNews, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position % 5 == 0) LARGE_NEWS_VIEW
        else SMALL_NEWS_VIEW

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == SMALL_NEWS_VIEW)
            SmallVH(
                ItemNewsHomeLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else
            LargeVH(
                ItemNewsHomeLargeItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SmallVH -> {
                holder.bind(getItem(position), position)
            }

            is LargeVH -> {
                holder.bind(getItem(position), position)
            }
        }
    }

    override fun submitList(list: List<ArticleModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}