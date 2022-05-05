package com.imzqqq.app.features.roomprofile.banned

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.dividerItem
import com.imzqqq.app.core.epoxy.profiles.buildProfileSection
import com.imzqqq.app.core.epoxy.profiles.profileMatrixItemWithProgress
import com.imzqqq.app.core.extensions.join
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.members.RoomMemberSummaryFilter
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class RoomBannedMemberListController @Inject constructor(
        private val avatarRenderer: AvatarRenderer,
        private val stringProvider: StringProvider,
        private val roomMemberSummaryFilter: RoomMemberSummaryFilter
) : TypedEpoxyController<RoomBannedMemberListViewState>() {

    interface Callback {
        fun onUnbanClicked(roomMember: RoomMemberSummary)
    }

    var callback: Callback? = null

    init {
        setData(null)
    }

    override fun buildModels(data: RoomBannedMemberListViewState?) {
        val bannedList = data?.bannedMemberSummaries?.invoke() ?: return
        val host = this

        val quantityString = stringProvider.getQuantityString(R.plurals.room_settings_banned_users_count, bannedList.size, bannedList.size)

        if (bannedList.isEmpty()) {
            buildProfileSection(stringProvider.getString(R.string.room_settings_banned_users_title))

            genericFooterItem {
                id("footer")
                text(quantityString)
            }
        } else {
            buildProfileSection(quantityString)

            roomMemberSummaryFilter.filter = data.filter
            bannedList
                    .filter { roomMemberSummaryFilter.test(it) }
                    .join(
                            each = { _, roomMember ->
                                val actionInProgress = data.onGoingModerationAction.contains(roomMember.userId)
                                profileMatrixItemWithProgress {
                                    id(roomMember.userId)
                                    matrixItem(roomMember.toMatrixItem())
                                    avatarRenderer(host.avatarRenderer)
                                    apply {
                                        if (actionInProgress) {
                                            inProgress(true)
                                            editable(false)
                                        } else {
                                            inProgress(false)
                                            editable(true)
                                            clickListener {
                                                host.callback?.onUnbanClicked(roomMember)
                                            }
                                        }
                                    }
                                }
                            },
                            between = { _, roomMemberBefore ->
                                dividerItem {
                                    id("divider_${roomMemberBefore.userId}")
                                }
                            }
                    )
        }
    }
}
