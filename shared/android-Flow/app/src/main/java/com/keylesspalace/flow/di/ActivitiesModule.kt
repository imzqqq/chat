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

import com.keylesspalace.flow.AboutActivity
import com.keylesspalace.flow.AccountActivity
import com.keylesspalace.flow.AccountListActivity
import com.keylesspalace.flow.BaseActivity
import com.keylesspalace.flow.EditProfileActivity
import com.keylesspalace.flow.FiltersActivity
import com.keylesspalace.flow.LicenseActivity
import com.keylesspalace.flow.ListsActivity
import com.keylesspalace.flow.LoginActivity
import com.keylesspalace.flow.MainActivity
import com.keylesspalace.flow.ModalTimelineActivity
import com.keylesspalace.flow.SplashActivity
import com.keylesspalace.flow.StatusListActivity
import com.keylesspalace.flow.TabPreferenceActivity
import com.keylesspalace.flow.ViewMediaActivity
import com.keylesspalace.flow.ViewTagActivity
import com.keylesspalace.flow.ViewThreadActivity
import com.keylesspalace.flow.components.announcements.AnnouncementsActivity
import com.keylesspalace.flow.components.compose.ComposeActivity
import com.keylesspalace.flow.components.drafts.DraftsActivity
import com.keylesspalace.flow.components.instancemute.InstanceListActivity
import com.keylesspalace.flow.components.preference.PreferencesActivity
import com.keylesspalace.flow.components.report.ReportActivity
import com.keylesspalace.flow.components.scheduled.ScheduledTootActivity
import com.keylesspalace.flow.components.search.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by charlag on 3/24/18.
 */

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun contributesBaseActivity(): BaseActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesAccountActivity(): AccountActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesListsActivity(): ListsActivity

    @ContributesAndroidInjector
    abstract fun contributesComposeActivity(): ComposeActivity

    @ContributesAndroidInjector
    abstract fun contributesEditProfileActivity(): EditProfileActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesAccountListActivity(): AccountListActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesModalTimelineActivity(): ModalTimelineActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesViewTagActivity(): ViewTagActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesViewThreadActivity(): ViewThreadActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesStatusListActivity(): StatusListActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesSearchAvtivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun contributesAboutActivity(): AboutActivity

    @ContributesAndroidInjector
    abstract fun contributesLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributesSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesPreferencesActivity(): PreferencesActivity

    @ContributesAndroidInjector
    abstract fun contributesViewMediaActivity(): ViewMediaActivity

    @ContributesAndroidInjector
    abstract fun contributesLicenseActivity(): LicenseActivity

    @ContributesAndroidInjector
    abstract fun contributesTabPreferenceActivity(): TabPreferenceActivity

    @ContributesAndroidInjector
    abstract fun contributesFiltersActivity(): FiltersActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesReportActivity(): ReportActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesInstanceListActivity(): InstanceListActivity

    @ContributesAndroidInjector
    abstract fun contributesScheduledTootActivity(): ScheduledTootActivity

    @ContributesAndroidInjector
    abstract fun contributesAnnouncementsActivity(): AnnouncementsActivity

    @ContributesAndroidInjector
    abstract fun contributesDraftActivity(): DraftsActivity
}
