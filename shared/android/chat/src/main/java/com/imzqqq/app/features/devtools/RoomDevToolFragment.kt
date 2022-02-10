package com.imzqqq.app.features.devtools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import javax.inject.Inject

class RoomDevToolFragment @Inject constructor(
        private val epoxyController: RoomDevToolRootController
) : VectorBaseFragment<FragmentGenericRecyclerBinding>(),
        DevToolsInteractionListener {

    private val sharedViewModel: RoomDevToolViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
        epoxyController.interactionListener = this

//        sharedViewModel.observeViewEvents {
//            when (it) {
//                is DevToolsViewEvents.showJson -> {
//                    JSonViewerDialog.newInstance(it.jsonString, -1, createJSonViewerStyleProvider(colorProvider))
//                            .show(childFragmentManager, "JSON_VIEWER")
//
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        epoxyController.interactionListener = null
        super.onDestroyView()
    }

    override fun processAction(action: RoomDevToolAction) {
        sharedViewModel.handle(action)
    }
}
