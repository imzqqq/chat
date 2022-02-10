package com.imzqqq.app.features.call

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.mvrx.activityViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetCallControlsBinding

@AndroidEntryPoint
class CallControlsBottomSheet : VectorBaseBottomSheetDialogFragment<BottomSheetCallControlsBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetCallControlsBinding {
        return BottomSheetCallControlsBinding.inflate(inflater, container, false)
    }

    private val callViewModel: VectorCallViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callViewModel.onEach {
            renderState(it)
        }

        views.callControlsSwitchCamera.views.bottomSheetActionClickableZone.debouncedClicks {
            callViewModel.handle(VectorCallViewActions.ToggleCamera)
            dismiss()
        }

        views.callControlsToggleSDHD.views.bottomSheetActionClickableZone.debouncedClicks {
            callViewModel.handle(VectorCallViewActions.ToggleHDSD)
            dismiss()
        }

        views.callControlsToggleHoldResume.views.bottomSheetActionClickableZone.debouncedClicks {
            callViewModel.handle(VectorCallViewActions.ToggleHoldResume)
            dismiss()
        }

        views.callControlsOpenDialPad.views.bottomSheetActionClickableZone.debouncedClicks {
            callViewModel.handle(VectorCallViewActions.OpenDialPad)
        }

        views.callControlsTransfer.views.bottomSheetActionClickableZone.debouncedClicks {
            callViewModel.handle(VectorCallViewActions.InitiateCallTransfer)
            dismiss()
        }
    }

    private fun renderState(state: VectorCallViewState) {
        views.callControlsSwitchCamera.isVisible = state.isVideoCall && state.canSwitchCamera
        views.callControlsSwitchCamera.subTitle = getString(if (state.isFrontCamera) R.string.call_camera_front else R.string.call_camera_back)
        if (state.isVideoCall) {
            views.callControlsToggleSDHD.isVisible = true
            if (state.isHD) {
                views.callControlsToggleSDHD.title = getString(R.string.call_format_turn_hd_off)
                views.callControlsToggleSDHD.subTitle = null
                views.callControlsToggleSDHD.leftIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_hd_disabled)
            } else {
                views.callControlsToggleSDHD.title = getString(R.string.call_format_turn_hd_on)
                views.callControlsToggleSDHD.subTitle = null
                views.callControlsToggleSDHD.leftIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_hd)
            }
        } else {
            views.callControlsToggleSDHD.isVisible = false
        }
        if (state.isRemoteOnHold) {
            views.callControlsToggleHoldResume.title = getString(R.string.call_resume_action)
            views.callControlsToggleHoldResume.subTitle = null
            views.callControlsToggleHoldResume.leftIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_call_resume_action)
        } else {
            views.callControlsToggleHoldResume.title = getString(R.string.call_hold_action)
            views.callControlsToggleHoldResume.subTitle = null
            views.callControlsToggleHoldResume.leftIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_call_hold_action)
        }
        views.callControlsTransfer.isVisible = state.canOpponentBeTransferred
    }
}
