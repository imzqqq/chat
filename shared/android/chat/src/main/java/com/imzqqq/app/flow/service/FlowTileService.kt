package com.imzqqq.app.flow.service

import android.annotation.TargetApi
import android.content.Intent
import android.service.quicksettings.TileService
import com.imzqqq.app.flow.FlowActivity

/**
 * Small Addition that adds in a QuickSettings tile
 * opens the Compose activity or shows an account selector when multiple accounts are present
 */
@TargetApi(24)
class FlowTileService : TileService() {

    override fun onClick() {
        val intent = Intent(this, FlowActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_SEND
            type = "text/plain"
        }
        startActivityAndCollapse(intent)
    }
}
