/* Copyright 2020 Flow Contributors
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

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.emoji.text.EmojiCompat
import com.keylesspalace.flow.util.LocaleManager
import de.c1710.filemojicompat.FileEmojiCompatConfig

// override FlowApplication for Robolectric tests, only initialize the necessary stuff
class FlowApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EmojiCompat.init(FileEmojiCompatConfig(this, ""))
    }

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
    }

    companion object {
        @JvmStatic
        lateinit var localeManager: LocaleManager
    }
}
