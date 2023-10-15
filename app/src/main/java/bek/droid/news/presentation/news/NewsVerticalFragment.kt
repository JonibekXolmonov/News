package bek.droid.news.presentation.news

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SnapHelper
import bek.droid.news.R
import bek.droid.news.app.activity.MainActivity
import bek.droid.news.common.Constants
import bek.droid.news.common.util.openNewsInBrowser
import bek.droid.news.common.util.shareLinkAsText
import bek.droid.news.common.util.shareScreenShot
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentNewsVerticalBinding
import bek.droid.news.presentation.adapter.NewsDetailAdapter
import bek.droid.news.presentation.bookmark.BookmarkDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsVerticalFragment : Fragment() {

    private var _binding: FragmentNewsVerticalBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailsViewModelImpl>()
    private val adapter by lazy { NewsDetailAdapter() }
    private val args: NewsVerticalFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (args.newsList.isEmpty())
            viewModel.news()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsVerticalBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initNewsObserver()
        initIvBookmarkState()
        initScreenshotObserver()
    }

    private fun initIvBookmarkState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ivBookmarkState.collectLatest { isSaved ->
                    binding.ivBookmark.setImageResource(if (isSaved) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark)
                }
            }
        }
    }

    private fun initNewsObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.news.collectLatest { news ->
                    if (news.isNotEmpty())
                        refreshAdapter(news)
                }
            }
        }
    }

    private fun initScreenshotObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenshotUri.collectLatest { uri: Uri? ->
                    uri?.let {
                        shareScreenShot(bitmapUri = uri)
                    }
                }
            }
        }
    }

    private fun refreshAdapter(news: List<ArticleModel>) {
        adapter.submitList(news)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(1)
                binding.rvNews.scrollToPosition(args.position)
            }
        }
    }

    private fun initViews() {
        with(binding) {
            rvNews.adapter = adapter

            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvNews)

            if (args.newsList.isNotEmpty())
                refreshAdapter(args.newsList.toList())

            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            ivScreenshotShare.setOnClickListener {
                viewModel.captureScreenshot(rvNews)
            }

            ivBookmark.setOnClickListener {
                adapter.visibleArticleModel?.let {
                    BookmarkDialog(it).show(childFragmentManager, Constants.TAG)
                }
            }

            ivShare.setOnClickListener {
                shareLinkAsText(adapter.getCurrentNewsLink())
            }
        }
        adapter.onNewsReadMore = {
            openNewsInBrowser(it.url)
        }

        setScrollListener()
    }

    private fun setScrollListener() {
        binding.rvNews.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItemPos =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                viewModel.bookmarkState(adapter.currentList[currentItemPos].isSaved)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}