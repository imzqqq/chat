package com.imzqqq.app.core.epoxy

import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.core.platform.DefaultListUpdateCallback
import com.imzqqq.app.core.platform.Restorable
import java.util.concurrent.atomic.AtomicReference

private const val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"

class LayoutManagerStateRestorer(layoutManager: RecyclerView.LayoutManager) : Restorable, DefaultListUpdateCallback {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var layoutManagerState = AtomicReference<Parcelable?>()

    init {
        this.layoutManager = layoutManager
    }

    fun clear() {
        layoutManager = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val layoutManagerState = layoutManager?.onSaveInstanceState()
        outState.putParcelable(LAYOUT_MANAGER_STATE, layoutManagerState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val parcelable = savedInstanceState?.getParcelable<Parcelable>(LAYOUT_MANAGER_STATE)
        layoutManagerState.set(parcelable)
    }

    override fun onInserted(position: Int, count: Int) {
        layoutManagerState.getAndSet(null)?.also {
            layoutManager?.onRestoreInstanceState(it)
        }
    }
}
