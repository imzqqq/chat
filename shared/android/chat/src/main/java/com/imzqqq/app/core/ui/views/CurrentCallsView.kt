package com.imzqqq.app.core.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ViewCurrentCallsBinding
import com.imzqqq.app.features.call.webrtc.WebRtcCall
import com.imzqqq.app.features.themes.ThemeUtils
import org.matrix.android.sdk.api.session.call.CallState

class CurrentCallsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    interface Callback {
        fun onTapToReturnToCall()
    }

    val views: ViewCurrentCallsBinding
    var callback: Callback? = null

    init {
        inflate(context, R.layout.view_current_calls, this)
        views = ViewCurrentCallsBinding.bind(this)
        setBackgroundColor(ThemeUtils.getColor(context, R.attr.colorPrimary))
        val outValue = TypedValue().also {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true)
        }
        foreground = AppCompatResources.getDrawable(context, outValue.resourceId)
        setOnClickListener { callback?.onTapToReturnToCall() }
    }

    fun render(calls: List<WebRtcCall>, formattedDuration: String) {
        val tapToReturnFormat = if (calls.size == 1) {
            val firstCall = calls.first()
            when (firstCall.mxCall.state) {
                is CallState.Idle,
                is CallState.CreateOffer,
                is CallState.LocalRinging,
                is CallState.Dialing   -> {
                    resources.getString(R.string.call_ringing)
                }
                is CallState.Answering -> {
                    resources.getString(R.string.call_connecting)
                }
                else                   -> {
                    resources.getString(R.string.call_one_active, formattedDuration)
                }
            }
        } else {
            resources.getQuantityString(R.plurals.call_active_status, calls.size, calls.size)
        }
        views.currentCallsInfo.text = resources.getString(R.string.call_tap_to_return, tapToReturnFormat)
    }
}
