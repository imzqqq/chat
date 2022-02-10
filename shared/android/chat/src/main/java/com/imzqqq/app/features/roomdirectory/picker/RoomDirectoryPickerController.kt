package com.imzqqq.app.features.roomdirectory.picker

import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.dividerItem
import com.imzqqq.app.core.epoxy.errorWithRetryItem
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.extensions.join
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.verticalMarginItem
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.features.discovery.settingsContinueCancelItem
import com.imzqqq.app.features.discovery.settingsInformationItem
import com.imzqqq.app.features.form.formEditTextItem
import com.imzqqq.app.features.roomdirectory.RoomDirectoryData
import com.imzqqq.app.features.roomdirectory.RoomDirectoryServer
import org.matrix.android.sdk.api.failure.Failure
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class RoomDirectoryPickerController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val dimensionConverter: DimensionConverter,
        private val errorFormatter: ErrorFormatter
) : TypedEpoxyController<RoomDirectoryPickerViewState>() {

    var currentRoomDirectoryData: RoomDirectoryData? = null
    var callback: Callback? = null

    override fun buildModels(data: RoomDirectoryPickerViewState) {
        val host = this

        when (val asyncThirdPartyProtocol = data.asyncThirdPartyRequest) {
            is Success    -> {
                data.directories.join(
                        each = { _, roomDirectoryServer -> buildDirectory(roomDirectoryServer) },
                        between = { idx, _ -> buildDivider(idx) }
                )
                buildForm(data)
                verticalMarginItem {
                    id("space_bottom")
                    heightInPx(host.dimensionConverter.dpToPx(16))
                }
            }
            is Incomplete -> {
                loadingItem {
                    id("loading")
                }
            }
            is Fail       -> {
                errorWithRetryItem {
                    id("error")
                    text(host.errorFormatter.toHumanReadable(asyncThirdPartyProtocol.error))
                    listener { host.callback?.retry() }
                }
            }
        }
    }

    private fun buildForm(data: RoomDirectoryPickerViewState) {
        buildDivider(1000)
        val host = this
        if (data.inEditMode) {
            verticalMarginItem {
                id("form_space")
                heightInPx(host.dimensionConverter.dpToPx(16))
            }
            settingsInformationItem {
                id("form_notice")
                message(host.stringProvider.getString(R.string.directory_add_a_new_server_prompt))
                textColor(host.colorProvider.getColor(R.color.vector_info_color))
            }
            verticalMarginItem {
                id("form_space_2")
                heightInPx(host.dimensionConverter.dpToPx(8))
            }
            formEditTextItem {
                id("edit")
                value(data.enteredServer)
                imeOptions(EditorInfo.IME_ACTION_DONE)
                editorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (data.enteredServer.isNotEmpty()) {
                                host.callback?.onSubmitServer()
                            }
                            return true
                        }
                        return false
                    }
                })
                hint(host.stringProvider.getString(R.string.directory_server_placeholder))
                inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI)
                onTextChange { text ->
                    host.callback?.onEnterServerChange(text)
                }
                when (data.addServerAsync) {
                    Uninitialized -> enabled(true)
                    is Loading    -> enabled(false)
                    is Success    -> enabled(false)
                    is Fail       -> {
                        enabled(true)
                        errorMessage(host.getErrorMessage(data.addServerAsync.error))
                    }
                }
            }
            when (data.addServerAsync) {
                Uninitialized,
                is Fail    -> settingsContinueCancelItem {
                    id("continueCancel")
                    continueText(host.stringProvider.getString(R.string.ok))
                    canContinue(data.enteredServer.isNotEmpty())
                    continueOnClick { host.callback?.onSubmitServer() }
                    cancelOnClick { host.callback?.onCancelEnterServer() }
                }
                is Loading -> loadingItem {
                    id("addLoading")
                }
                is Success -> Unit /* This is a transitive state */
            }
        } else {
            genericButtonItem {
                id("add")
                text(host.stringProvider.getString(R.string.directory_add_a_new_server))
                textColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
                buttonClickAction {
                    host.callback?.onStartEnterServer()
                }
            }
        }
    }

    private fun getErrorMessage(error: Throwable): String {
        return if (error is Failure.ServerError &&
                error.httpCode == HttpsURLConnection.HTTP_INTERNAL_ERROR /* 500 */) {
            stringProvider.getString(R.string.directory_add_a_new_server_error)
        } else {
            errorFormatter.toHumanReadable(error)
        }
    }

    private fun buildDivider(idx: Int) {
        dividerItem {
            id("divider_$idx")
        }
    }

    private fun buildDirectory(roomDirectoryServer: RoomDirectoryServer) {
        val host = this
        roomDirectoryServerItem {
            id("server_$roomDirectoryServer")
            serverName(roomDirectoryServer.serverName)
            canRemove(roomDirectoryServer.isManuallyAdded)
            removeListener { host.callback?.onRemoveServer(roomDirectoryServer) }

            if (roomDirectoryServer.isUserServer) {
                serverDescription(host.stringProvider.getString(R.string.directory_your_server))
            }
        }

        roomDirectoryServer.protocols.forEach { roomDirectoryData ->
            roomDirectoryItem {
                id("server_${roomDirectoryServer}_proto_$roomDirectoryData")
                directoryName(
                        if (roomDirectoryData.includeAllNetworks) {
                            host.stringProvider.getString(R.string.directory_server_all_rooms_on_server, roomDirectoryServer.serverName)
                        } else {
                            roomDirectoryData.displayName
                        }
                )
                if (roomDirectoryData.displayName == RoomDirectoryData.MATRIX_PROTOCOL_NAME && !roomDirectoryData.includeAllNetworks) {
                    directoryDescription(
                            host.stringProvider.getString(R.string.directory_server_native_rooms, roomDirectoryServer.serverName)
                    )
                }
                directoryAvatarUrl(roomDirectoryData.avatarUrl)
                includeAllNetworks(roomDirectoryData.includeAllNetworks)
                checked(roomDirectoryData == host.currentRoomDirectoryData)
                globalListener {
                    host.callback?.onRoomDirectoryClicked(roomDirectoryData)
                }
            }
        }
    }

    interface Callback {
        fun onRoomDirectoryClicked(roomDirectoryData: RoomDirectoryData)
        fun retry()
        fun onStartEnterServer()
        fun onEnterServerChange(server: String)
        fun onSubmitServer()
        fun onCancelEnterServer()
        fun onRemoveServer(roomDirectoryServer: RoomDirectoryServer)
    }
}
