package bek.droid.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import bek.droid.news.common.formatDate
import bek.droid.news.common.isNotNull
import bek.droid.news.common.loadWithFresco
import bek.droid.news.common.setSpannableText
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.NewsDetailLayoutBinding

class NewsDetailAdapter : ListAdapter<ArticleModel, NewsDetailAdapter.VH>(DiffUtil()) {

    lateinit var onNewsClick: (ArticleModel) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class VH(private val binding: NewsDetailLayoutBinding) :
        ViewHolder(binding.root) {
        fun bind(article: ArticleModel) {
            with(binding) {
                ivNews.loadWithFresco(article.urlToImage)

                tvSourceName.text =
                    if (article.source?.name.isNotNull()) article.source?.name else article.author

                tvPublishedDate.text = article.publishedAt?.formatDate()

                tvTitle.text = article.title

                var originalText =
                    if (!article.content.isNullOrBlank()) article.content else article.description
                if (originalText.isNullOrBlank()) originalText = article.title

                if (article.url.isNotNull()) {
                    tvDescription.setSpannableText(originalText) {
                        onNewsClick(article)
                    }
                } else {
                    tvDescription.text = originalText
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(
            NewsDetailLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}