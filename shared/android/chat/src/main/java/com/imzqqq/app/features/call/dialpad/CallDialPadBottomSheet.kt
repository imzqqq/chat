package com.imzqqq.app.features.call.dialpad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addChildFragment
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetCallDialPadBinding
import com.imzqqq.app.features.settings.VectorLocale

class CallDialPadBottomSheet : VectorBaseBottomSheetDialogFragment<BottomSheetCallDialPadBinding>() {

    companion object {

        private const val EXTRA_SHOW_ACTIONS = "EXTRA_SHOW_ACTIONS"

        fun newInstance(showActions: Boolean): CallDialPadBottomSheet {
            return CallDialPadBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_SHOW_ACTIONS, showActions)
                }
            }
        }
    }

    override val showExpanded = true

    var callback: DialPadFragment.Callback? = null
        set(value) {
            field = value
            setCallbackToFragment(callback)
        }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetCallDialPadBinding {
        return BottomSheetCallDialPadBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val showActions = arguments?.getBoolean(EXTRA_SHOW_ACTIONS, false) ?: false
            DialPadFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(DialPadFragment.EXTRA_ENABLE_DELETE, showActions)
                    putBoolean(DialPadFragment.EXTRA_ENABLE_OK, showActions)
                    putBoolean(DialPadFragment.EXTRA_CURSOR_VISIBLE, false)
                    putString(DialPadFragment.EXTRA_REGION_CODE, VectorLocale.applicationLocale.country)
                }
                callback = DialPadFragmentCallbackWrapper(this@CallDialPadBottomSheet.callback)
            }.also {
                addChildFragment(R.id.callDialPadFragmentContainer, it)
            }
        } else {
            setCallbackToFragment(callback)
        }
        views.callDialPadClose.debouncedClicks {
            dismiss()
        }
    }

    override fun onDestroyView() {
        setCallbackToFragment(null)
        super.onDestroyView()
    }

    private fun setCallbackToFragment(callback: DialPadFragment.Callback?) {
        if (!isAdded) return
        val dialPadFragment = childFragmentManager.findFragmentById(R.id.callDialPadFragmentContainer) as? DialPadFragment
        dialPadFragment?.callback = DialPadFragmentCallbackWrapper(callback)
    }

    private inner class DialPadFragmentCallbackWrapper(val callback: DialPadFragment.Callback?) : DialPadFragment.Callback {

        override fun onDigitAppended(digit: String) {
            callback?.onDigitAppended(digit)
        }

        override fun onOkClicked(formatted: String?, raw: String?) {
            callback?.onOkClicked(formatted, raw)
            dismiss()
        }
    }
}
