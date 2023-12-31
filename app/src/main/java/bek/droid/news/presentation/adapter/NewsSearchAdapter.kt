package bek.droid.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import bek.droid.news.R
import bek.droid.news.common.util.formatDate
import bek.droid.news.common.util.loadWithFresco
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.ItemNewsHomeLayoutBinding

class NewsSearchAdapter : ListAdapter<ArticleModel, NewsSearchAdapter.VH>(DiffUtil()) {

    lateinit var onNewsClick: (Int) -> Unit
    lateinit var onBookmarkAction: (ArticleModel) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }
    }


    inner class VH(private val binding: ItemNewsHomeLayoutBinding) : ViewHolder(binding.root) {
        fun bind(article: ArticleModel, position: Int) {
            with(binding) {
                ivNews.loadWithFresco(article.urlToImage)
                tvTitle.text = article.title
                tvSourceName.text = article.source?.name
                tvPublishedDate.text = article.publishedAt?.formatDate()
                ivBookmark.setImageResource(if (article.isSaved) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark)

                ivBookmark.setOnClickListener {
                    onBookmarkAction(article)
                }

                root.setOnClickListener {
                    onNewsClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemNewsHomeLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun submitList(list: List<ArticleModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}