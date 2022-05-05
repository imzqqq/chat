package com.imzqqq.app.features.debug.sas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import org.matrix.android.sdk.api.crypto.getAllVerificationEmojis

class DebugSasEmojiActivity : AppCompatActivity() {

    private lateinit var views: FragmentGenericRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = FragmentGenericRecyclerBinding.inflate(layoutInflater)
        setContentView(views.root)
        val controller = SasEmojiController()
        views.genericRecyclerView.configureWith(controller)
        controller.setData(SasState(getAllVerificationEmojis()))
    }

    override fun onDestroy() {
        views.genericRecyclerView.cleanup()
        super.onDestroy()
    }
}
