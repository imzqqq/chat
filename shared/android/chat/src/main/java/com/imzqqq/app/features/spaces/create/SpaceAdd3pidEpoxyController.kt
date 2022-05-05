package com.imzqqq.app.features.spaces.create

import android.text.InputType
import com.airbnb.epoxy.TypedEpoxyController
import com.google.android.material.textfield.TextInputLayout
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.ItemStyle
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericPillItem
import com.imzqqq.app.features.form.formEditTextItem
import javax.inject.Inject

class SpaceAdd3pidEpoxyController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider
) : TypedEpoxyController<CreateSpaceState>() {

    var listener: Listener? = null

    override fun buildModels(data: CreateSpaceState?) {
        val host = this
        data ?: return
        genericFooterItem {
            id("info_help_header")
            style(ItemStyle.TITLE)
            text(host.stringProvider.getString(R.string.create_spaces_invite_public_header))
            textColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_content_primary))
        }
        genericFooterItem {
            id("info_help_desc")
            text(host.stringProvider.getString(R.string.create_spaces_invite_public_header_desc, data.name ?: ""))
            textColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary))
        }

        if (data.canInviteByMail) {
            buildEmailFields(data, host)
        } else {
            genericPillItem {
                id("no_IDS")
                imageRes(R.drawable.ic_baseline_perm_contact_calendar_24)
                text(host.stringProvider.getString(R.string.create_space_identity_server_info_none))
            }
            genericButtonItem {
                id("Discover_Settings")
                text(host.stringProvider.getString(R.string.open_discovery_settings))
                textColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
                buttonClickAction {
                    host.listener?.onNoIdentityServer()
                }
            }
        }
    }

    private fun buildEmailFields(data: CreateSpaceState, host: SpaceAdd3pidEpoxyController) {
        for (index in 0..2) {
            val mail = data.default3pidInvite?.get(index)
            formEditTextItem {
                id("3pid$index")
                enabled(true)
                value(mail)
                hint(host.stringProvider.getString(R.string.medium_email))
                inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                endIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
                errorMessage(
                        if (data.emailValidationResult?.get(index) == false) {
                            host.stringProvider.getString(R.string.does_not_look_like_valid_email)
                        } else null
                )
                onTextChange { text ->
                    host.listener?.on3pidChange(index, text)
                }
            }
        }
    }

    interface Listener {
        fun on3pidChange(index: Int, newName: String)
        fun onNoIdentityServer()
    }
}
