package com.imzqqq.app.features.spaces.people

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.dividerItem
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.epoxy.profiles.profileMatrixItemWithPowerLevel
import com.imzqqq.app.core.extensions.join
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericItem
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.members.RoomMemberListCategories
import com.imzqqq.app.features.roomprofile.members.RoomMemberListViewState
import com.imzqqq.app.features.roomprofile.members.RoomMemberSummaryFilter
import me.gujun.android.span.span
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class SpacePeopleListController @Inject constructor(
        private val avatarRenderer: AvatarRenderer,
        private val colorProvider: ColorProvider,
        private val stringProvider: StringProvider,
        private val dimensionConverter: DimensionConverter,
        private val roomMemberSummaryFilter: RoomMemberSummaryFilter
) : TypedEpoxyController<RoomMemberListViewState>() {

    interface InteractionListener {
        fun onSpaceMemberClicked(roomMemberSummary: RoomMemberSummary)
        fun onInviteToSpaceSelected()
    }

    var listener: InteractionListener? = null

    init {
        setData(null)
    }

    override fun buildModels(data: RoomMemberListViewState?) {
        val host = this
        val memberSummaries = data?.roomMemberSummaries?.invoke()
        if (memberSummaries == null) {
            loadingItem { id("loading") }
            return
        }
        roomMemberSummaryFilter.filter = data.filter
        var foundCount = 0
        memberSummaries.forEach { memberEntry ->

            val filtered = memberEntry.second
                    .filter { roomMemberSummaryFilter.test(it.roomMemberSummary) }
            if (filtered.isNotEmpty()) {
                dividerItem {
                    id("divider_type_${memberEntry.first.titleRes}")
                }
            }
            foundCount += filtered.size
            filtered
                    .join(
                            each = { _, roomMemberWrapper ->
                                val roomMember = roomMemberWrapper.roomMemberSummary
                                profileMatrixItemWithPowerLevel {
                                    id(roomMember.userId)
                                    matrixItem(roomMember.toMatrixItem())
                                    avatarRenderer(host.avatarRenderer)
                                    userEncryptionTrustLevel(data.trustLevelMap.invoke()?.get(roomMember.userId))
                                            .apply {
                                                val pl = host.toPowerLevelLabel(memberEntry.first)
                                                if (memberEntry.first == RoomMemberListCategories.INVITE) {
                                                    powerLevelLabel(
                                                            span {
                                                                span(host.stringProvider.getString(R.string.invited)) {
                                                                    textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                                                    textStyle = "bold"
                                                                    // fontFamily = "monospace"
                                                                }
                                                            }
                                                    )
                                                } else if (pl != null) {
                                                    powerLevelLabel(
                                                            span {
                                                                span(" $pl ") {
                                                                    backgroundColor = host.colorProvider.getColor(R.color.notification_accent_color)
                                                                    paddingTop = host.dimensionConverter.dpToPx(2)
                                                                    paddingBottom = host.dimensionConverter.dpToPx(2)
                                                                    textColor = host.colorProvider.getColorFromAttribute(R.attr.colorOnPrimary)
                                                                    textStyle = "bold"
                                                                    // fontFamily = "monospace"
                                                                }
                                                            }
                                                    )
                                                } else {
                                                    powerLevelLabel(null)
                                                }
                                            }

                                    clickListener {
                                        host.listener?.onSpaceMemberClicked(roomMember)
                                    }
                                }
                            },
                            between = { _, roomMemberBefore ->
                                dividerItem {
                                    id("divider_${roomMemberBefore.roomMemberSummary.userId}")
                                }
                            }
                    )
        }

        if (foundCount == 0 && data.filter.isNotEmpty()) {
            // add the footer thing
            genericItem {
                id("not_found")
                title(
                        span {
                            +"\n"
                            +host.stringProvider.getString(R.string.no_result_placeholder)
                        }
                )
                description(
                        span {
                            +host.stringProvider.getString(R.string.looking_for_someone_not_in_space, data.roomSummary.invoke()?.displayName ?: "")
                            +"\n"
                            span("Invite them") {
                                textColor = host.colorProvider.getColorFromAttribute(R.attr.colorPrimary)
                                textStyle = "bold"
                            }
                        }
                )
                itemClickAction {
                    host.listener?.onInviteToSpaceSelected()
                }
            }
        }
    }

    private fun toPowerLevelLabel(categories: RoomMemberListCategories): String? {
        return when (categories) {
            RoomMemberListCategories.ADMIN     -> stringProvider.getString(R.string.power_level_admin)
            RoomMemberListCategories.MODERATOR -> stringProvider.getString(R.string.power_level_moderator)
            else                               -> null
        }
    }
}
