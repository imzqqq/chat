package com.imzqqq.app.features.settings.push

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import org.matrix.android.sdk.api.session.pushers.Pusher
import javax.inject.Inject

// Referenced in vector_settings_notifications.xml
class PushGatewaysFragment @Inject constructor(
        private val epoxyController: PushGateWayController
) : VectorBaseFragment<FragmentGenericRecyclerBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: PushGatewaysViewModel by fragmentViewModel(PushGatewaysViewModel::class)

    override fun getMenuRes() = R.menu.menu_push_gateways

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.handle(PushGatewayAction.Refresh)
                true
            }
            else         ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.settings_notifications_targets)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyController.interactionListener = object : PushGatewayItemInteractions {
            override fun onRemovePushTapped(pusher: Pusher) = viewModel.handle(PushGatewayAction.RemovePusher(pusher))
        }
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
        viewModel.observeViewEvents {
            when (it) {
                is PushGatewayViewEvents.RemovePusherFailed -> {
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.dialog_title_error)
                            .setMessage(errorFormatter.toHumanReadable(it.cause))
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                }
            }.exhaustive
        }
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }
}
