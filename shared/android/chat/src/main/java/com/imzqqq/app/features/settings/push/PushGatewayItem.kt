package com.imzqqq.app.features.settings.push

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.extensions.setTextOrHide
import org.matrix.android.sdk.api.session.pushers.Pusher

@EpoxyModelClass(layout = R.layout.item_pushgateway)
abstract class PushGatewayItem : EpoxyModelWithHolder<PushGatewayItem.Holder>() {

    @EpoxyAttribute
    lateinit var pusher: Pusher

    @EpoxyAttribute
    lateinit var interactions: PushGatewayItemInteractions

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.kind.text = when (pusher.kind) {
            Pusher.KIND_HTTP  -> "Http Pusher"
            Pusher.KIND_EMAIL -> "Email Pusher"
            else              -> pusher.kind
        }

        holder.appId.text = pusher.appId
        holder.pushKey.text = pusher.pushKey
        holder.appName.text = pusher.appDisplayName
        holder.url.setTextOrHide(pusher.data.url, hideWhenBlank = true, holder.urlTitle)
        holder.format.setTextOrHide(pusher.data.format, hideWhenBlank = true, holder.formatTitle)
        holder.deviceName.text = pusher.deviceDisplayName
        holder.removeButton.setOnClickListener {
            interactions.onRemovePushTapped(pusher)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val kind by bind<TextView>(R.id.pushGatewayKind)
        val pushKey by bind<TextView>(R.id.pushGatewayKeyValue)
        val deviceName by bind<TextView>(R.id.pushGatewayDeviceNameValue)
        val formatTitle by bind<View>(R.id.pushGatewayFormat)
        val format by bind<TextView>(R.id.pushGatewayFormatValue)
        val urlTitle by bind<View>(R.id.pushGatewayURL)
        val url by bind<TextView>(R.id.pushGatewayURLValue)
        val appName by bind<TextView>(R.id.pushGatewayAppNameValue)
        val appId by bind<TextView>(R.id.pushGatewayAppIdValue)
        val removeButton by bind<View>(R.id.pushGatewayDeleteButton)
    }
}

interface PushGatewayItemInteractions {
    fun onRemovePushTapped(pusher: Pusher)
}

//
// abstract class ReactionInfoSimpleItem : EpoxyModelWithHolder<ReactionInfoSimpleItem.Holder>() {
