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

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RawRes
import com.keylesspalace.flow.databinding.ActivityLicenseBinding
import com.keylesspalace.flow.util.IOUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LicenseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setTitle(R.string.title_licenses)

        loadFileIntoTextView(R.raw.apache, binding.licenseApacheTextView)
    }

    private fun loadFileIntoTextView(@RawRes fileId: Int, textView: TextView) {

        val sb = StringBuilder()

        val br = BufferedReader(InputStreamReader(resources.openRawResource(fileId)))

        try {
            var line: String? = br.readLine()
            while (line != null) {
                sb.append(line)
                sb.append('\n')
                line = br.readLine()
            }
        } catch (e: IOException) {
            Log.w("LicenseActivity", e)
        }

        IOUtils.closeQuietly(br)

        textView.text = sb.toString()
    }
}
