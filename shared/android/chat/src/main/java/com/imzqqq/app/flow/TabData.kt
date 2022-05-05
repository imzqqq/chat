package com.imzqqq.app.flow

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.imzqqq.app.R
import com.imzqqq.app.flow.components.conversation.ConversationsFragment
import com.imzqqq.app.flow.components.timeline.TimelineFragment
import com.imzqqq.app.flow.components.timeline.TimelineViewModel
import com.imzqqq.app.flow.fragment.NotificationsFragment

/** This would be a good case for a sealed class, but that does not work nice with Room */
const val HOME = "Home"
const val NOTIFICATIONS = "Notifications"
const val LOCAL = "Local"
const val FEDERATED = "Federated"
const val DIRECT = "Direct"
const val HASHTAG = "Hashtag"
const val LIST = "List"

data class TabData(
    val id: String,
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
    val fragment: (List<String>) -> Fragment,
    val arguments: List<String> = emptyList(),
    val title: (Context) -> String = { context -> context.getString(text) }
)

fun createTabDataFromId(id: String, arguments: List<String> = emptyList()): TabData {
    return when (id) {
        HOME -> TabData(
            HOME,
            R.string.title_home,
            R.drawable.ic_home_24dp,
            { TimelineFragment.newInstance(TimelineViewModel.Kind.HOME) }
        )
        NOTIFICATIONS -> TabData(
            NOTIFICATIONS,
            R.string.title_notifications,
            R.drawable.ic_notifications_24dp,
            { NotificationsFragment.newInstance() }
        )
        LOCAL -> TabData(
            LOCAL,
            R.string.title_public_local,
            R.drawable.ic_local_24dp,
            { TimelineFragment.newInstance(TimelineViewModel.Kind.PUBLIC_LOCAL) }
        )
        FEDERATED -> TabData(
            FEDERATED,
            R.string.title_public_federated,
            R.drawable.ic_public_24dp,
            { TimelineFragment.newInstance(TimelineViewModel.Kind.PUBLIC_FEDERATED) }
        )
        DIRECT -> TabData(
            DIRECT,
            R.string.title_direct_messages,
            R.drawable.ic_reblog_direct_24dp,
            { ConversationsFragment.newInstance() }
        )
        HASHTAG -> TabData(
            HASHTAG,
            R.string.hashtags,
            R.drawable.ic_hashtag,
            { args -> TimelineFragment.newHashtagInstance(args) },
            arguments,
            { context -> arguments.joinToString(separator = " ") { context.getString(R.string.title_tag, it) } }
        )
        LIST -> TabData(
            LIST,
            R.string.list,
            R.drawable.ic_list,
            { args -> TimelineFragment.newInstance(TimelineViewModel.Kind.LIST, args.getOrNull(0).orEmpty()) },
            arguments,
            { arguments.getOrNull(1).orEmpty() }
        )
        else -> throw IllegalArgumentException("unknown tab type")
    }
}

fun defaultTabs(): List<TabData> {
    return listOf(
        createTabDataFromId(HOME),
        createTabDataFromId(NOTIFICATIONS),
        createTabDataFromId(LOCAL),
        createTabDataFromId(FEDERATED)
    )
}
