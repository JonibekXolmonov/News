package bek.droid.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bek.droid.news.common.convertToDate
import bek.droid.news.common.load
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.ItemNewsHomeLayoutBinding
import com.squareup.picasso.Picasso

class NewsMainAdapter : ListAdapter<ArticleModel, NewsMainAdapter.VH>(DiffUtil()) {

    lateinit var onTaskClick: (ArticleModel) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class VH(private val binding: ItemNewsHomeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleModel) {
            binding.ivNews.load(article.urlToImage)
            binding.tvTitle.text = article.title
            binding.tvSourceName.text = article.source.name
            binding.tvPublishedDate.text = article.publishedAt?.convertToDate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemNewsHomeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}