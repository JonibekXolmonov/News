package bek.droid.news.presentation.bookmark

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import bek.droid.news.R
import bek.droid.news.app.activity.MainActivity
import bek.droid.news.common.Constants
import bek.droid.news.common.enums.SavedNewsFilter
import bek.droid.news.common.enums.UiStateList
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.FragmentBookmarkBinding
import bek.droid.news.databinding.FragmentHomeBinding
import bek.droid.news.presentation.adapter.NewsSearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.nio.channels.Channel

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SavedNewsViewModelImpl>()
    private val adapter by lazy { NewsSearchAdapter() }
    lateinit var menuHost: MenuHost

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.savedNews()
        (requireActivity() as MainActivity).window.statusBarColor = Color.parseColor("#A5A5A5")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost = requireActivity()
        initViews()
        initReceiver()
        initOptionsMenu()
    }

    private fun initOptionsMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.bookmark_filter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_bookmark -> {
                        true
                    }

                    R.id.menu_readLater -> {
                        true
                    }

                    R.id.menu_important -> {
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initReceiver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedNews.collectLatest { state ->
                    when (state) {
                        UiStateList.EMPTY -> {

                        }

                        is UiStateList.ERROR -> {

                        }

                        UiStateList.LOADING -> {

                        }

                        is UiStateList.SUCCESS -> {
                            refreshAdapter(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun refreshAdapter(data: List<ArticleModel>) {
        adapter.submitList(data)
    }

    private fun initViews() {
        with(binding) {
            rvNewsMain.adapter = adapter

            adapter.onBookmarkAction = {
                BookmarkDialog(it).show(childFragmentManager, Constants.TAG)
            }

            adapter.onNewsClick = {
                val route = BookmarkFragmentDirections.actionBookmarkFragmentToNewsVerticalFragment(
                    newsList = (viewModel.savedNews.value as UiStateList.SUCCESS).data.toTypedArray(),
                    position = it
                )

                findNavController().navigate(route)
            }
        }
    }
}