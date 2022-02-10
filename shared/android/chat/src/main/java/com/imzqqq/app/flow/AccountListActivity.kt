package com.imzqqq.app.flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityAccountListBinding
import com.imzqqq.app.flow.fragment.AccountListFragment

@AndroidEntryPoint
class AccountListActivity : BaseActivity() {

    enum class Type {
        FOLLOWS,
        FOLLOWERS,
        BLOCKS,
        MUTES,
        FOLLOW_REQUESTS,
        REBLOGGED,
        FAVOURITED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAccountListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getSerializableExtra(EXTRA_TYPE) as Type
        val id: String? = intent.getStringExtra(EXTRA_ID)
        val accountLocked: Boolean = intent.getBooleanExtra(EXTRA_ACCOUNT_LOCKED, false)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.apply {
            when (type) {
                Type.BLOCKS          -> setTitle(R.string.title_blocks)
                Type.MUTES           -> setTitle(R.string.title_mutes)
                Type.FOLLOW_REQUESTS -> setTitle(R.string.title_follow_requests)
                Type.FOLLOWERS       -> setTitle(R.string.title_followers)
                Type.FOLLOWS         -> setTitle(R.string.title_follows)
                Type.REBLOGGED       -> setTitle(R.string.title_reblogged_by)
                Type.FAVOURITED      -> setTitle(R.string.title_favourited_by)
            }
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, AccountListFragment.newInstance(type, id, accountLocked))
            .commit()
    }

    companion object {
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_ID = "id"
        private const val EXTRA_ACCOUNT_LOCKED = "acc_locked"

        @JvmStatic
        @JvmOverloads
        fun newIntent(context: Context, type: Type, id: String? = null, accountLocked: Boolean = false): Intent {
            return Intent(context, AccountListActivity::class.java).apply {
                putExtra(EXTRA_TYPE, type)
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_ACCOUNT_LOCKED, accountLocked)
            }
        }
    }
}
