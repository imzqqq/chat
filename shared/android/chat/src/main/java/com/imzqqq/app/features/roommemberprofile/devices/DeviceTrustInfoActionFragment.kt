package com.imzqqq.app.features.roommemberprofile.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.databinding.BottomSheetGenericListBinding
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo
import javax.inject.Inject

class DeviceTrustInfoActionFragment @Inject constructor(
        val dimensionConverter: DimensionConverter,
        val epoxyController: DeviceTrustInfoEpoxyController
) : VectorBaseFragment<BottomSheetGenericListBinding>(),
        DeviceTrustInfoEpoxyController.InteractionListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetGenericListBinding {
        return BottomSheetGenericListBinding.inflate(inflater, container, false)
    }

    private val viewModel: DeviceListBottomSheetViewModel by parentFragmentViewModel(DeviceListBottomSheetViewModel::class)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.bottomSheetRecyclerView.setPadding(0, dimensionConverter.dpToPx(16), 0, dimensionConverter.dpToPx(16))
        views.bottomSheetRecyclerView.configureWith(
                epoxyController,
                hasFixedSize = false
        )
        epoxyController.interactionListener = this
    }

    override fun onDestroyView() {
        views.bottomSheetRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) {
        epoxyController.setData(it)
    }

    override fun onVerifyManually(device: CryptoDeviceInfo) {
        viewModel.handle(DeviceListAction.ManuallyVerify(device.deviceId))
    }
}
