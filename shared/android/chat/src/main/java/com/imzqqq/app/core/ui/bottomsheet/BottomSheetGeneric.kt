package com.imzqqq.app.core.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetGenericListBinding
import javax.inject.Inject

/**
 * Generic Bottom sheet with actions
 */
abstract class BottomSheetGeneric<STATE : BottomSheetGenericState, ACTION : BottomSheetGenericRadioAction> :
        VectorBaseBottomSheetDialogFragment<BottomSheetGenericListBinding>(),
        BottomSheetGenericController.Listener<ACTION> {

    @Inject lateinit var sharedViewPool: RecyclerView.RecycledViewPool

    final override val showExpanded = true

    final override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetGenericListBinding {
        return BottomSheetGenericListBinding.inflate(inflater, container, false)
    }

    abstract fun getController(): BottomSheetGenericController<STATE, ACTION>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.bottomSheetRecyclerView.configureWith(getController(), viewPool = sharedViewPool, hasFixedSize = false, disableItemAnimation = true)
        getController().listener = this
    }

    @CallSuper
    override fun onDestroyView() {
        views.bottomSheetRecyclerView.cleanup()
        getController().listener = null
        super.onDestroyView()
    }
}
