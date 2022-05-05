package com.imzqqq.app.features.settings.devtools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import javax.inject.Inject
class OutgoingKeyRequestListFragment @Inject constructor(
        private val epoxyController: OutgoingKeyRequestPagedController
) : VectorBaseFragment<FragmentGenericRecyclerBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: KeyRequestListViewModel by fragmentViewModel(KeyRequestListViewModel::class)

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.submitList(state.outgoingRoomKeyRequests.invoke())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
//        epoxyController.interactionListener = this
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
//        epoxyController.interactionListener = null
        super.onDestroyView()
    }
}
