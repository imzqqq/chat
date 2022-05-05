package com.imzqqq.app.flow

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RawRes
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityLicenseBinding
import com.imzqqq.app.flow.util.IOUtils
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@AndroidEntryPoint
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
            Timber.tag("LicenseActivity").w(e)
        }

        IOUtils.closeQuietly(br)

        textView.text = sb.toString()
    }
}
