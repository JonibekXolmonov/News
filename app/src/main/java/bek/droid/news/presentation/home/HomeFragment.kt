package bek.droid.news.presentation.home

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bek.droid.news.app.activity.MainActivity
import bek.droid.news.common.Constants.NO_URL_ATTACHED
import bek.droid.news.common.Constants.TAG
import bek.droid.news.common.enums.UiStateList
import bek.droid.news.common.util.fadeVisibility
import bek.droid.news.common.util.hide
import bek.droid.news.common.util.loadWithLoadingThumb
import bek.droid.news.common.util.shareLinkAsText
import bek.droid.news.common.util.show
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentHomeBinding
import bek.droid.news.presentation.adapter.NewsMainAdapter
import bek.droid.news.presentation.bookmark.BookmarkDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModelImpl>()
    private val adapter by lazy { NewsMainAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.news()
        viewModel.fetchNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initObserver()
        initNewNewsObserver()

        rvScrollState()

        binding.rvNewsMain.itemAnimator = null
        binding.rvNewsMain.adapter = adapter

        binding.alertView.alertView.setOnClickListener {
            setAlertVisibility(visibility = View.GONE)
            viewModel.updateNewNewsState()
            binding.rvNewsMain.smoothScrollToPosition(0)
        }

        binding.ivSearch.setOnClickListener {
            (requireActivity() as MainActivity).setSearchAsSelected()
        }

        binding.tvTitle.setOnClickListener {
            viewModel.fetchNews()
        }

        adapter.onNewsClick = { _, image, position ->
            val route = HomeFragmentDirections.actionHomeFragmentToNewsVerticalFragment(
                position = position,
                newsList = emptyArray()
            )

            findNavController().navigate(route)
        }

        adapter.onBookmarkAction = {
            BookmarkDialog(it).show(childFragmentManager, TAG)
        }

        adapter.onShareAction = {
            shareLinkAsText(it.url ?: NO_URL_ATTACHED)
        }
    }

    private fun rvScrollState() {
        binding.rvNewsMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    viewModel.updateNewNewsState()
                    setAlertVisibility(visibility = View.GONE)
                }
            }
        })
    }

    private fun initNewNewsObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newNewsAvailable.collect { newNewsState ->
                    if (newNewsState.isAvailable) {
                        binding.alertView.image1.ivNews.loadWithLoadingThumb(newNewsState.news[0].urlToImage)
                        binding.alertView.image2.ivNews.loadWithLoadingThumb(newNewsState.news[1].urlToImage)
                        binding.alertView.image3.ivNews.loadWithLoadingThumb(newNewsState.news[2].urlToImage)

                        setAlertVisibility(
                            visibility = View.VISIBLE,
                            duration = 250
                        )
                    }
                }
            }
        }
    }

    private fun setAlertVisibility(visibility: Int, duration: Long = 500) {
        binding.alertView.alertView.fadeVisibility(visibility, duration = duration)
    }

    private fun refreshAdapter(news: List<ArticleModel>) {
        adapter.submitList(news)
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsState.collect { state ->
                    when (state) {
                        UiStateList.EMPTY -> {
                        }

                        UiStateList.LOADING -> {
                            showLoading()
                        }

                        is UiStateList.SUCCESS -> {
                            val news = state.data
                            refreshAdapter(news)
                            hideLoading()
                        }

                        is UiStateList.ERROR -> {
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        binding.loading.hide()
    }

    private fun showLoading() {
        binding.loading.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}