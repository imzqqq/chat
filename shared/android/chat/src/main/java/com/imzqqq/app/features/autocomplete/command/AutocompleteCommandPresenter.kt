package com.imzqqq.app.features.autocomplete.command

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.features.autocomplete.AutocompleteClickListener
import com.imzqqq.app.features.autocomplete.RecyclerViewPresenter
import com.imzqqq.app.features.command.Command
import com.imzqqq.app.features.settings.VectorPreferences
import javax.inject.Inject

class AutocompleteCommandPresenter @Inject constructor(context: Context,
                                                       private val controller: AutocompleteCommandController,
                                                       private val vectorPreferences: VectorPreferences) :
        RecyclerViewPresenter<Command>(context), AutocompleteClickListener<Command> {

    init {
        controller.listener = this
    }

    override fun instantiateAdapter(): RecyclerView.Adapter<*> {
        return controller.adapter
    }

    override fun onItemClick(t: Command) {
        dispatchClick(t)
    }

    override fun onQuery(query: CharSequence?) {
        val data = Command.values()
                .filter {
                    !it.isDevCommand || vectorPreferences.developerMode()
                }
                .filter {
                    if (query.isNullOrEmpty()) {
                        true
                    } else {
                        it.command.startsWith(query, 1, true)
                    }
                }
        controller.setData(data)
    }

    fun clear() {
        controller.listener = null
    }
}
