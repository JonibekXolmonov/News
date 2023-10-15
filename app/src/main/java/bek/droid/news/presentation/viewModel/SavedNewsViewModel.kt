package bek.droid.news.presentation.viewModel

import bek.droid.news.common.enums.SavedNewsFilter

interface SavedNewsViewModel {

    fun savedNews(filter: SavedNewsFilter = SavedNewsFilter.SAVED)

}