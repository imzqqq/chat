/* Copyright 2018 Jeremiasz Nelz <remi6397(a)gmail.com>
 * Copyright 2018 Conny Duck
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

import com.keylesspalace.flow.receiver.NotificationClearBroadcastReceiver
import com.keylesspalace.flow.receiver.SendStatusBroadcastReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributeSendStatusBroadcastReceiver(): SendStatusBroadcastReceiver

    @ContributesAndroidInjector
    abstract fun contributeNotificationClearBroadcastReceiver(): NotificationClearBroadcastReceiver
}
