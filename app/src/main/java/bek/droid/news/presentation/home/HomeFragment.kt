package bek.droid.news.presentation.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bek.droid.news.app.activity.MainActivity
import bek.droid.news.common.UiStateList
import bek.droid.news.common.fadeVisibility
import bek.droid.news.common.hide
import bek.droid.news.common.loadWithLoadingThumb
import bek.droid.news.common.show
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentHomeBinding
import bek.droid.news.presentation.adapter.NewsMainAdapter
import dagger.hilt.android.AndroidEntryPoint
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
        (requireActivity() as MainActivity).window.statusBarColor = Color.parseColor("#A5A5A5")
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
        initNewNewsObserver()

        rvScrollState()

        binding.rvNewsMain.adapter = adapter

        binding.alertView.alertView.setOnClickListener {
            setAlertVisibility(visibility = View.GONE)
            viewModel.updateNewNewsState()
            binding.rvNewsMain.smoothScrollToPosition(0)
        }

        binding.ivSearch.setOnClickListener {
            viewModel.fetchNews()
        }

        adapter.onNewsClick = {
            val route = HomeFragmentDirections.actionHomeFragmentToNewsVerticalFragment(myArg = viewModel.newsFromLocal.toTypedArray(), position = it)
            findNavController().navigate(route)
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
//                            showMessage(state.message)
                        }

                        is UiStateList.PAGING_END -> {
                            hideLoading()
                            //  showMessage("You have reached to end!")
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