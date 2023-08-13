package bek.droid.news.presentation.news

import android.content.Context
import android.graphics.Color
import android.net.Uri
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import bek.droid.news.app.activity.MainActivity
import bek.droid.news.common.openNewsInBrowser
import bek.droid.news.common.shareScreenShot
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentNewsVerticalBinding
import bek.droid.news.presentation.adapter.NewsDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
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
        (requireActivity() as MainActivity).window.statusBarColor = Color.parseColor("#000000")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsVerticalBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initScreenshotObserver()
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
    }

    private fun initViews() {
        with(binding) {
            rvNews.adapter = adapter
            refreshAdapter(args.myArg.toList())
            rvNews.scrollToPosition(args.position)

            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvNews)

            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            ivScreenshotShare.setOnClickListener {
                viewModel.captureScreenshot(container)
            }
        }
        adapter.onNewsClick = {
            openNewsInBrowser(it.url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}