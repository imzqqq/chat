package com.imzqqq.app.flow.components.instancemute

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityAccountListBinding
import com.imzqqq.app.flow.BaseActivity
import com.imzqqq.app.flow.components.instancemute.fragment.InstanceListFragment

@AndroidEntryPoint
class InstanceListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAccountListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_account_list)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.apply {
            setTitle(R.string.title_domain_mutes)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, InstanceListFragment())
            .commit()
    }
}
