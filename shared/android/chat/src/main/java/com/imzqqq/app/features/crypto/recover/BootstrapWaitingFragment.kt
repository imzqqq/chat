package com.imzqqq.app.features.crypto.recover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentBootstrapWaitingBinding
import javax.inject.Inject

class BootstrapWaitingFragment @Inject constructor() :
    VectorBaseFragment<FragmentBootstrapWaitingBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBootstrapWaitingBinding {
        return FragmentBootstrapWaitingBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: BootstrapSharedViewModel by parentFragmentViewModel()

    override fun invalidate() = withState(sharedViewModel) { state ->
        when (state.step) {
            is BootstrapStep.Initializing -> {
                views.bootstrapLoadingStatusText.isVisible = true
                views.bootstrapDescriptionText.isVisible = true
                views.bootstrapLoadingStatusText.text = state.initializationWaitingViewData?.message
            }
//            is BootstrapStep.CheckingMigration -> {
//                bootstrapLoadingStatusText.isVisible = false
//                bootstrapDescriptionText.isVisible = false
//            }
            else                          -> {
                // just show the spinner
                views.bootstrapLoadingStatusText.isVisible = false
                views.bootstrapDescriptionText.isVisible = false
            }
        }
    }
}
