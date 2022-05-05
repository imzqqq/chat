package com.imzqqq.app.flow.di

import dagger.Binds
import com.imzqqq.app.flow.AccountsInListFragment
import com.imzqqq.app.flow.components.conversation.ConversationsFragment
import com.imzqqq.app.flow.components.instancemute.fragment.InstanceListFragment
import com.imzqqq.app.flow.components.preference.AccountPreferencesFragment
import com.imzqqq.app.flow.components.preference.NotificationPreferencesFragment
import com.imzqqq.app.flow.components.preference.PreferencesFragment
import com.imzqqq.app.flow.components.report.fragments.ReportDoneFragment
import com.imzqqq.app.flow.components.report.fragments.ReportNoteFragment
import com.imzqqq.app.flow.components.report.fragments.ReportStatusesFragment
import com.imzqqq.app.flow.components.search.fragments.SearchAccountsFragment
import com.imzqqq.app.flow.components.search.fragments.SearchHashtagsFragment
import com.imzqqq.app.flow.components.search.fragments.SearchStatusesFragment
import com.imzqqq.app.flow.components.timeline.TimelineFragment
import com.imzqqq.app.flow.fragment.AccountListFragment
import com.imzqqq.app.flow.fragment.AccountMediaFragment
import com.imzqqq.app.flow.fragment.NotificationsFragment
import com.imzqqq.app.flow.fragment.ViewThreadFragment
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import com.imzqqq.app.core.di.FragmentKey
import com.imzqqq.app.features.home.room.list.RoomListFragment

@InstallIn(ActivityComponent::class)
@Module
interface FlowFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(AccountListFragment::class)
    fun bindAccountListFragment(fragment: AccountListFragment): AccountListFragment

    @Binds
    @IntoMap
    @FragmentKey(AccountMediaFragment::class)
    fun bindAccountMediaFragment(fragment: AccountMediaFragment): AccountMediaFragment

    @Binds
    @IntoMap
    @FragmentKey(ViewThreadFragment::class)
    fun bindViewThreadFragment(fragment: ViewThreadFragment): ViewThreadFragment

    @Binds
    @IntoMap
    @FragmentKey(TimelineFragment::class)
    fun bindTimelineFragment(fragment: TimelineFragment): TimelineFragment

    @Binds
    @IntoMap
    @FragmentKey(NotificationsFragment::class)
    fun bindNotificationsFragment(fragment: NotificationsFragment): NotificationsFragment

    @Binds
    @IntoMap
    @FragmentKey(SearchStatusesFragment::class)
    fun bindSearchStatusesFragment(fragment: SearchStatusesFragment): SearchStatusesFragment

    @Binds
    @IntoMap
    @FragmentKey(NotificationPreferencesFragment::class)
    fun bindNotificationPreferencesFragment(fragment: NotificationPreferencesFragment): NotificationPreferencesFragment

    @Binds
    @IntoMap
    @FragmentKey(AccountPreferencesFragment::class)
    fun bindAccountPreferencesFragment(fragment: AccountPreferencesFragment): AccountPreferencesFragment

    @Binds
    @IntoMap
    @FragmentKey(ConversationsFragment::class)
    fun bindConversationsFragment(fragment: ConversationsFragment): ConversationsFragment

    @Binds
    @IntoMap
    @FragmentKey(AccountsInListFragment::class)
    fun bindAccountsInListFragment(fragment: AccountsInListFragment): AccountsInListFragment

    @Binds
    @IntoMap
    @FragmentKey(ReportStatusesFragment::class)
    fun bindReportStatusesFragment(fragment: ReportStatusesFragment): ReportStatusesFragment

    @Binds
    @IntoMap
    @FragmentKey(ReportNoteFragment::class)
    fun bindReportNoteFragment(fragment: ReportNoteFragment): ReportNoteFragment

    @Binds
    @IntoMap
    @FragmentKey(ReportDoneFragment::class)
    fun bindReportDoneFragment(fragment: ReportDoneFragment): ReportDoneFragment

    @Binds
    @IntoMap
    @FragmentKey(InstanceListFragment::class)
    fun bindInstanceListFragment(fragment: InstanceListFragment): InstanceListFragment

    @Binds
    @IntoMap
    @FragmentKey(SearchAccountsFragment::class)
    fun bindSearchAccountsFragment(fragment: SearchAccountsFragment): SearchAccountsFragment

    @Binds
    @IntoMap
    @FragmentKey(SearchHashtagsFragment::class)
    fun bindSearchHashtagsFragment(fragment: SearchHashtagsFragment): SearchHashtagsFragment

    @Binds
    @IntoMap
    @FragmentKey(PreferencesFragment::class)
    fun bindPreferencesFragment(fragment: PreferencesFragment): PreferencesFragment
}
