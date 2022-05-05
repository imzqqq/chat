/* Copyright 2018 Conny Duck
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

package com.keylesspalace.flow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keylesspalace.flow.components.notifications.NotificationHelper
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.di.Injectable
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** delete old notification channels */
        NotificationHelper.deleteLegacyNotificationChannels(this, accountManager)

        /** Determine whether the user is currently logged in, and if so go ahead and load the
         *  timeline. Otherwise, start the activity_login screen. */

        val intent = if (accountManager.activeAccount != null) {
            Intent(this, MainActivity::class.java)
        } else {
            LoginActivity.getIntent(this, false)
        }
        startActivity(intent)
        finish()
    }
}
