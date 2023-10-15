package bek.droid.news.presentation.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bek.droid.news.R
import bek.droid.news.common.util.showMessage
import bek.droid.news.data.model.ui_model.ArticleModel
import bek.droid.news.databinding.DialogBookmarkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkDialog(private val articleModel: ArticleModel) :
    BottomSheetDialogFragment(R.layout.dialog_bookmark) {

    private var _binding: DialogBookmarkBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BookmarkViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleModel.id?.let { id ->
            viewModel.isSavedInImportant(id)
            viewModel.isSavedInReadLater(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initCollector()
    }

    private fun initCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isInReadLater.collectLatest {
                    binding.readLaterBookmark.icBookmark.setImageResource(if (it) R.drawable.ic_added else R.drawable.ic_add)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isInImportant.collectLatest {
                    binding.importantBookmark.icBookmark.setImageResource(if (it) R.drawable.ic_added else R.drawable.ic_add)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSaved.collectLatest {
                    articleModel.isSaved = it
                    binding.defaultBookmark.icBookmark.setImageResource(if (it) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark)
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun initViews() {
        with(binding) {
            if (articleModel.isSaved) {
                defaultBookmark.icBookmark.setImageResource(R.drawable.ic_bookmark_added)
            }

            defaultBookmark.icBookmark.setOnClickListener {
                viewModel.controlSaving(articleModel)
            }

            readLaterBookmark.icBookmark.setOnClickListener {
                viewModel.removeIfExitsElseAddToReadLater(articleModel)
            }

            importantBookmark.icBookmark.setOnClickListener {
                viewModel.removeIfExitsElseAddToImportant(articleModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}