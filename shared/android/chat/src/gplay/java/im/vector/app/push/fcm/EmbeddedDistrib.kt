package com.imzqqq.app.push.fcm

import android.content.Context
import org.unifiedpush.android.embedded_fcm_distributor.GetEndpointHandler
import org.unifiedpush.android.embedded_fcm_distributor.EmbeddedDistributorReceiver

val handlerFCM = object: GetEndpointHandler {
    override fun getEndpoint(context: Context?, token: String, instance: String): String {
        // Here token is the FCM Token, used by the gateway (sygnal)
        return token
    }
}

class EmbeddedDistrib: EmbeddedDistributorReceiver(handlerFCM)
