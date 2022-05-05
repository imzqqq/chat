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
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.utils.createJSonViewerStyleProvider
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import org.billcarsonfr.jsonviewer.JSonViewerDialog
import org.matrix.android.sdk.api.session.events.model.Event
import javax.inject.Inject

class GossipingEventsPaperTrailFragment @Inject constructor(
        private val epoxyController: GossipingTrailPagedEpoxyController,
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentGenericRecyclerBinding>(),
        GossipingTrailPagedEpoxyController.InteractionListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: GossipingEventsPaperTrailViewModel by fragmentViewModel(GossipingEventsPaperTrailViewModel::class)

    override fun invalidate() = withState(viewModel) { state ->
        state.events.invoke()?.let {
            epoxyController.submitList(it)
        }
        Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
        epoxyController.interactionListener = this
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        epoxyController.interactionListener = null
        super.onDestroyView()
    }

    override fun didTap(event: Event) {
        if (event.isEncrypted()) {
            event.toClearContentStringWithIndent()
        } else {
            event.toContentStringWithIndent()
        }?.let {
            JSonViewerDialog.newInstance(
                    it,
                    -1,
                    createJSonViewerStyleProvider(colorProvider)
            ).show(childFragmentManager, "JSON_VIEWER")
        }
    }
}
