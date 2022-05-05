package com.imzqqq.app.features.roomprofile.members

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.dividerItem
import com.imzqqq.app.core.epoxy.profiles.buildProfileSection
import com.imzqqq.app.core.epoxy.profiles.profileMatrixItem
import com.imzqqq.app.core.epoxy.profiles.profileMatrixItemWithPowerLevelWithPresence
import com.imzqqq.app.core.extensions.join
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.home.AvatarRenderer
import me.gujun.android.span.span
import org.matrix.android.sdk.api.session.events.model.Event
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.session.room.model.RoomThirdPartyInviteContent
import org.matrix.android.sdk.api.util.MatrixItem
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class RoomMemberListController @Inject constructor(
        private val avatarRenderer: AvatarRenderer,
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val roomMemberSummaryFilter: RoomMemberSummaryFilter
) : TypedEpoxyController<RoomMemberListViewState>() {

    interface Callback {
        fun onRoomMemberClicked(roomMember: RoomMemberSummary)
        fun onThreePidInviteClicked(event: Event)
    }

    var callback: Callback? = null

    init {
        setData(null)
    }

    override fun buildModels(data: RoomMemberListViewState?) {
        data ?: return
        val host = this

        roomMemberSummaryFilter.filter = data.filter

        val roomMembersByPowerLevel = data.roomMemberSummaries.invoke() ?: return
        val filteredThreePidInvites = data.threePidInvites()
                ?.filter { event ->
                    event.content.toModel<RoomThirdPartyInviteContent>()
                            ?.takeIf {
                                data.filter.isEmpty() || it.displayName?.contains(data.filter, ignoreCase = true) == true
                            } != null
                }
                .orEmpty()
        var threePidInvitesDone = filteredThreePidInvites.isEmpty()
        var isFirstSection = true

        for ((powerLevelCategory, roomMemberList) in roomMembersByPowerLevel) {
            val filteredRoomMemberList = roomMemberList.filter { roomMemberSummaryFilter.test(it.roomMemberSummary) }
            if (filteredRoomMemberList.isEmpty()) {
                continue
            }

            /*
            if (powerLevelCategory == RoomMemberListCategories.USER && !threePidInvitesDone) {
                // If there is no regular invite, display threepid invite before the regular user
                buildProfileSection(
                        stringProvider.getString(RoomMemberListCategories.INVITE.titleRes)
                )

                buildThreePidInvites(filteredThreePidInvites, data.actionsPermissions.canRevokeThreePidInvite)
                threePidInvitesDone = true
            }
             */

            if (powerLevelCategory != RoomMemberListCategories.MEMBER || !isFirstSection) {
                buildProfileSection(
                        stringProvider.getString(powerLevelCategory.titleRes)
                )
            }
            isFirstSection = false

            filteredRoomMemberList.join(
                    each = { _, roomMember ->
                        buildRoomMember(roomMember.roomMemberSummary, roomMember.powerLevelCategory, host, data)
                    },
                    between = { _, roomMemberBefore ->
                        dividerItem {
                            id("divider_${roomMemberBefore.roomMemberSummary.userId}")
                        }
                    }
            )
            if (powerLevelCategory == RoomMemberListCategories.INVITE && !threePidInvitesDone) {
                // Display the threepid invite after the regular invite
                dividerItem {
                    id("divider_threepidinvites")
                }

                buildThreePidInvites(filteredThreePidInvites, data.actionsPermissions.canRevokeThreePidInvite)
                threePidInvitesDone = true
            }
        }

        if (!threePidInvitesDone) {
            // If there is not regular invite and no regular user, finally display threepid invite here
            buildProfileSection(
                    stringProvider.getString(RoomMemberListCategories.INVITE.titleRes)
            )

            buildThreePidInvites(filteredThreePidInvites, data.actionsPermissions.canRevokeThreePidInvite)
        }
    }

    private fun buildRoomMember(roomMember: RoomMemberSummary,
                                powerLevelCategory: RoomMemberListCategories,
                                host: RoomMemberListController,
                                data: RoomMemberListViewState) {
        val powerLabel = stringProvider.getString(powerLevelCategory.titleRes)

        profileMatrixItemWithPowerLevelWithPresence {
            id(roomMember.userId)
            matrixItem(roomMember.toMatrixItem())
            avatarRenderer(host.avatarRenderer)
            userEncryptionTrustLevel(data.trustLevelMap.invoke()?.get(roomMember.userId))
            clickListener {
                host.callback?.onRoomMemberClicked(roomMember)
            }
            userPresence(roomMember.userPresence)
            powerLevelLabel(
                    span {
                        span(powerLabel) {
                            textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                        }
                    }
            )
        }
    }

    private fun buildThreePidInvites(filteredThreePidInvites: List<Event>, canRevokeThreePidInvite: Boolean) {
        val host = this
        filteredThreePidInvites
                .join(
                        each = { idx, event ->
                            event.content.toModel<RoomThirdPartyInviteContent>()
                                    ?.let { content ->
                                        profileMatrixItem {
                                            id("3pid_$idx")
                                            matrixItem(MatrixItem.UserItem("@", displayName = content.displayName))
                                            avatarRenderer(host.avatarRenderer)
                                            editable(canRevokeThreePidInvite)
                                            clickListener {
                                                host.callback?.onThreePidInviteClicked(event)
                                            }
                                        }
                                    }
                        },
                        between = { idx, _ ->
                            dividerItem {
                                id("divider3_$idx")
                            }
                        }
                )
    }
}
