package com.imzqqq.app.features.popup

import android.app.Activity
import android.view.View
import com.imzqqq.app.R
import com.imzqqq.app.core.glide.GlideApp
import com.imzqqq.app.databinding.AlerterJitsiCallLayoutBinding
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

class JitsiCallAlert(uid: String,
                     override val shouldBeDisplayedIn: ((Activity) -> Boolean) = { true }
) : DefaultVectorAlert(uid, "", "", 0, shouldBeDisplayedIn) {

    override val priority = PopupAlertManager.JITSI_CALL_PRIORITY
    override val layoutRes = R.layout.alerter_jitsi_call_layout
    override var colorAttribute: Int? = R.attr.colorSurface
    override val dismissOnClick: Boolean = false
    override val isLight: Boolean = true

    class ViewBinder(private val matrixItem: MatrixItem?,
                     private val avatarRenderer: AvatarRenderer,
                     private val onJoin: () -> Unit) : VectorAlert.ViewBinder {

        override fun bind(view: View) {
            val views = AlerterJitsiCallLayoutBinding.bind(view)
            views.jitsiCallNameView.text = matrixItem?.getBestName()
            matrixItem?.let { avatarRenderer.render(it, views.jitsiCallAvatar, GlideApp.with(view.context.applicationContext)) }
            views.jitsiCallJoinView.setOnClickListener {
                onJoin()
            }
        }
    }
}
