package com.imzqqq.app.flow.components.report.adapter

import android.view.View
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.interfaces.LinkListener

interface AdapterHandler : LinkListener {
    fun showMedia(v: View?, status: Status?, idx: Int)
    fun setStatusChecked(status: Status, isChecked: Boolean)
    fun isStatusChecked(id: String): Boolean
}
