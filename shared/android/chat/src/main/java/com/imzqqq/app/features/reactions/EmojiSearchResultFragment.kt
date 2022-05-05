package com.imzqqq.app.features.reactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.LiveEvent
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import javax.inject.Inject

class EmojiSearchResultFragment @Inject constructor(
        private val epoxyController: EmojiSearchResultController
) : VectorBaseFragment<FragmentGenericRecyclerBinding>(), ReactionClickListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: EmojiSearchResultViewModel by activityViewModel()

    private lateinit var sharedViewModel: EmojiChooserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = activityViewModelProvider.get(EmojiChooserViewModel::class.java)
        epoxyController.listener = this
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
    }

    override fun onDestroyView() {
        epoxyController.listener = null
        views.genericRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun onReactionSelected(reaction: String) {
        sharedViewModel.selectedReaction = reaction
        sharedViewModel.navigateEvent.value = LiveEvent(EmojiChooserViewModel.NAVIGATE_FINISH)
    }

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }
}
