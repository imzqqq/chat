package com.imzqqq.app.features.home.room.detail.search

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.extensions.trackItemsVisibilityChange
import com.imzqqq.app.core.platform.StateView
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentSearchBinding
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.events.model.Event
import javax.inject.Inject

@Parcelize
data class SearchArgs(
        val roomId: String
) : Parcelable

class SearchFragment @Inject constructor(
        private val controller: SearchResultController
) : VectorBaseFragment<FragmentSearchBinding>(),
        StateView.EventCallback,
        SearchResultController.Listener {

    private val fragmentArgs: SearchArgs by args()
    private val searchViewModel: SearchViewModel by fragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.stateView.contentView = views.searchResultRecycler
        views.stateView.eventCallback = this

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        views.searchResultRecycler.trackItemsVisibilityChange()
        views.searchResultRecycler.configureWith(controller)
        (views.searchResultRecycler.layoutManager as? LinearLayoutManager)?.stackFromEnd = true
        controller.listener = this
    }

    override fun onDestroyView() {
        views.searchResultRecycler.cleanup()
        controller.listener = null
        super.onDestroyView()
    }

    override fun invalidate() = withState(searchViewModel) { state ->
        if (state.searchResult.isNullOrEmpty()) {
            when (state.asyncSearchRequest) {
                is Loading -> {
                    views.stateView.state = StateView.State.Loading
                }
                is Fail    -> {
                    views.stateView.state = StateView.State.Error(errorFormatter.toHumanReadable(state.asyncSearchRequest.error))
                }
                is Success -> {
                    views.stateView.state = StateView.State.Empty(
                            title = getString(R.string.search_no_results),
                            image = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search_no_results))
                }
            }
        } else {
            controller.setData(state)
            views.stateView.state = StateView.State.Content
        }
    }

    fun search(query: String) {
        view?.hideKeyboard()
        searchViewModel.handle(SearchAction.SearchWith(query))
    }

    override fun onRetryClicked() {
        searchViewModel.handle(SearchAction.Retry)
    }

    override fun onItemClicked(event: Event) {
        event.roomId?.let {
            navigator.openRoom(requireContext(), it, event.eventId)
        }
    }

    override fun loadMore() {
        searchViewModel.handle(SearchAction.LoadMore)
    }
}
