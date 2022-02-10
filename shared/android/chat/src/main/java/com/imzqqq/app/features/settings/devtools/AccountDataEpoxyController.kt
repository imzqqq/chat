package com.imzqqq.app.features.settings.devtools

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericWithValueItem
import org.matrix.android.sdk.api.session.accountdata.UserAccountDataEvent
import javax.inject.Inject

class AccountDataEpoxyController @Inject constructor(
        private val stringProvider: StringProvider
) : TypedEpoxyController<AccountDataViewState>() {

    interface InteractionListener {
        fun didTap(data: UserAccountDataEvent)
        fun didLongTap(data: UserAccountDataEvent)
    }

    var interactionListener: InteractionListener? = null

    override fun buildModels(data: AccountDataViewState?) {
        if (data == null) return
        val host = this
        when (data.accountData) {
            is Loading -> {
                loadingItem {
                    id("loading")
                    loadingText(host.stringProvider.getString(R.string.loading))
                }
            }
            is Fail    -> {
                genericFooterItem {
                    id("fail")
                    text(data.accountData.error.localizedMessage)
                }
            }
            is Success -> {
                val dataList = data.accountData.invoke()
                if (dataList.isEmpty()) {
                    genericFooterItem {
                        id("noResults")
                        text(host.stringProvider.getString(R.string.no_result_placeholder))
                    }
                } else {
                    dataList.forEach { accountData ->
                        genericWithValueItem {
                            id(accountData.type)
                            title(accountData.type)
                            itemClickAction {
                                host.interactionListener?.didTap(accountData)
                            }
                            itemLongClickAction(View.OnLongClickListener {
                                host.interactionListener?.didLongTap(accountData)
                                true
                            })
                        }
                    }
                }
            }
        }
    }
}
