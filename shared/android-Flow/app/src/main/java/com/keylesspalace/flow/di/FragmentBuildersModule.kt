/* Copyright 2018 charlag
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.di

import com.keylesspalace.flow.AccountsInListFragment
import com.keylesspalace.flow.components.conversation.ConversationsFragment
import com.keylesspalace.flow.components.instancemute.fragment.InstanceListFragment
import com.keylesspalace.flow.components.preference.AccountPreferencesFragment
import com.keylesspalace.flow.components.preference.NotificationPreferencesFragment
import com.keylesspalace.flow.components.preference.PreferencesFragment
import com.keylesspalace.flow.components.report.fragments.ReportDoneFragment
import com.keylesspalace.flow.components.report.fragments.ReportNoteFragment
import com.keylesspalace.flow.components.report.fragments.ReportStatusesFragment
import com.keylesspalace.flow.components.search.fragments.SearchAccountsFragment
import com.keylesspalace.flow.components.search.fragments.SearchHashtagsFragment
import com.keylesspalace.flow.components.search.fragments.SearchStatusesFragment
import com.keylesspalace.flow.components.timeline.TimelineFragment
import com.keylesspalace.flow.fragment.AccountListFragment
import com.keylesspalace.flow.fragment.AccountMediaFragment
import com.keylesspalace.flow.fragment.NotificationsFragment
import com.keylesspalace.flow.fragment.ViewThreadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by charlag on 3/24/18.
 */

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun accountListFragment(): AccountListFragment

    @ContributesAndroidInjector
    abstract fun accountMediaFragment(): AccountMediaFragment

    @ContributesAndroidInjector
    abstract fun viewThreadFragment(): ViewThreadFragment

    @ContributesAndroidInjector
    abstract fun timelineFragment(): TimelineFragment

    @ContributesAndroidInjector
    abstract fun notificationsFragment(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchStatusesFragment

    @ContributesAndroidInjector
    abstract fun notificationPreferencesFragment(): NotificationPreferencesFragment

    @ContributesAndroidInjector
    abstract fun accountPreferencesFragment(): AccountPreferencesFragment

    @ContributesAndroidInjector
    abstract fun directMessagesPreferencesFragment(): ConversationsFragment

    @ContributesAndroidInjector
    abstract fun accountInListsFragment(): AccountsInListFragment

    @ContributesAndroidInjector
    abstract fun reportStatusesFragment(): ReportStatusesFragment

    @ContributesAndroidInjector
    abstract fun reportNoteFragment(): ReportNoteFragment

    @ContributesAndroidInjector
    abstract fun reportDoneFragment(): ReportDoneFragment

    @ContributesAndroidInjector
    abstract fun instanceListFragment(): InstanceListFragment

    @ContributesAndroidInjector
    abstract fun searchAccountFragment(): SearchAccountsFragment

    @ContributesAndroidInjector
    abstract fun searchHashtagsFragment(): SearchHashtagsFragment

    @ContributesAndroidInjector
    abstract fun preferencesFragment(): PreferencesFragment
}
