package bek.droid.news.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import bek.droid.news.common.UiStateList
import bek.droid.news.common.hide
import bek.droid.news.common.listener.PaginationScrollListener
import bek.droid.news.common.show
import bek.droid.news.common.showMessage
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentHomeBinding
import bek.droid.news.presentation.adapter.NewsMainAdapter
import dagger.hilt.android.AndroidEntryPoint
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
        viewModel.fetchNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.rvNewsMain.adapter = adapter
        scrollListener()
    }

    private fun scrollListener() {
        binding.rvNewsMain.addOnScrollListener(object :
            PaginationScrollListener(binding.rvNewsMain.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.fetchNews()
            }

            override val isLastPage: Boolean
                get() = viewModel.newsState.value == UiStateList.PAGING_END
            override val isLoading: Boolean
                get() = viewModel.newsState.value == UiStateList.LOADING
        })
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsState.collectLatest { state ->
                    when (state) {
                        UiStateList.EMPTY -> {}

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

                        is UiStateList.PAGING_END -> {
                            hideLoading()
                            showMessage("You have reached to end!")
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

    private fun refreshAdapter(news: List<ArticleModel>) {
        adapter.submitList(news)
        adapter.onTaskClick = {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}