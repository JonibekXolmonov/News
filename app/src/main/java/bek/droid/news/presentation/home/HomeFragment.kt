package bek.droid.news.presentation.home

import android.content.Context
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
import bek.droid.news.common.UiStateList
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
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsState.collectLatest { state ->
                    when (state) {
                        UiStateList.EMPTY -> {}

                        UiStateList.LOADING -> {}

                        is UiStateList.SUCCESS -> {
                            val articles = state.data
                            refreshAdapter(articles)
                        }

                        is UiStateList.ERROR -> {

                        }
                    }
                }
            }
        }
    }

    private fun refreshAdapter(articles: List<ArticleModel>) {
        Log.d("TAG", "refreshAdapter: $articles")
        adapter.submitList(articles)
        binding.rvNewsMain.adapter = adapter
        adapter.onTaskClick = {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}