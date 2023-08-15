package bek.droid.news.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import bek.droid.news.common.UiStateList
import bek.droid.news.common.hide
import bek.droid.news.common.isNotEmpty
import bek.droid.news.common.show
import bek.droid.news.common.showMessage
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentSearchBinding
import bek.droid.news.presentation.adapter.NewsSearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModelImpl>()
    private var _binding: FragmentSearchBinding? = null
    private val adapter by lazy { NewsSearchAdapter() }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
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
                            hideEmptyAnimation()

                            if (news.isEmpty() && binding.edtSearch.isNotEmpty()) {
                                showEmptyAnimation()
                            }
                        }

                        is UiStateList.ERROR -> {
                            hideLoading()
                        }

                        is UiStateList.PAGING_END -> {
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun showEmptyAnimation() {
        binding.empty.playAnimation()
        binding.empty.show()
    }


    private fun hideEmptyAnimation() {
        binding.empty.hide()
    }

    private fun showLoading() {
        binding.loading.show()
    }

    private fun hideLoading() {
        binding.loading.hide()
    }

    private fun refreshAdapter(news: List<ArticleModel>) {
        adapter.submitList(news)
    }

    private fun initViews() {
        binding.rvNews.adapter = adapter

        adapter.onNewsClick = {
            val route = SearchFragmentDirections.actionSearchFragmentToNewsVerticalFragment(
                myArg = adapter.currentList.toTypedArray(),
                position = it
            )

            findNavController().navigate(route)
        }

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.search(query = text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}