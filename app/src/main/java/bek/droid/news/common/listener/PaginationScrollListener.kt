package bek.droid.news.common.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean
}
//usa case
//    private fun scrollListener() {
//        binding.rvNewsMain.addOnScrollListener(object :
//            PaginationScrollListener(binding.rvNewsMain.layoutManager as LinearLayoutManager) {
//            override fun loadMoreItems() {
//                viewModel.fetchNews()
//            }
//
//            override val isLastPage: Boolean
//                get() = viewModel.newsState.value == UiStateList.PAGING_END
//            override val isLoading: Boolean
//                get() = viewModel.newsState.value == UiStateList.LOADING
//        })
//    }