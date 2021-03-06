package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.interfaces.AccountActionListener
import com.imzqqq.app.flow.util.removeDuplicates

/** Generic adapter with bottom loading indicator. */
abstract class AccountAdapter<AVH : RecyclerView.ViewHolder> internal constructor(
        var accountActionListener: AccountActionListener,
        protected val animateAvatar: Boolean,
        protected val animateEmojis: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var accountList = mutableListOf<Account>()
    private var bottomLoading: Boolean = false

    override fun getItemCount(): Int {
        return accountList.size + if (bottomLoading) 1 else 0
    }

    abstract fun createAccountViewHolder(parent: ViewGroup): AVH

    abstract fun onBindAccountViewHolder(viewHolder: AVH, position: Int)

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ACCOUNT) {
            @Suppress("UNCHECKED_CAST")
            this.onBindAccountViewHolder(holder as AVH, position)
        }
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ACCOUNT -> this.createAccountViewHolder(parent)
            VIEW_TYPE_FOOTER -> this.createFooterViewHolder(parent)
            else -> error("Unknown item type: $viewType")
        }
    }

    private fun createFooterViewHolder(
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_footer, parent, false)
        return LoadingFooterViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == accountList.size && bottomLoading) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_ACCOUNT
        }
    }

    fun update(newAccounts: List<Account>) {
        accountList = removeDuplicates(newAccounts)
        notifyDataSetChanged()
    }

    fun addItems(newAccounts: List<Account>) {
        val end = accountList.size
        val last = accountList[end - 1]
        if (newAccounts.none { it.id == last.id }) {
            accountList.addAll(newAccounts)
            notifyItemRangeInserted(end, newAccounts.size)
        }
    }

    fun setBottomLoading(loading: Boolean) {
        val wasLoading = bottomLoading
        if (wasLoading == loading) {
            return
        }
        bottomLoading = loading
        if (loading) {
            notifyItemInserted(accountList.size)
        } else {
            notifyItemRemoved(accountList.size)
        }
    }

    fun removeItem(position: Int): Account? {
        if (position < 0 || position >= accountList.size) {
            return null
        }
        val account = accountList.removeAt(position)
        notifyItemRemoved(position)
        return account
    }

    fun addItem(account: Account, position: Int) {
        if (position < 0 || position > accountList.size) {
            return
        }
        accountList.add(position, account)
        notifyItemInserted(position)
    }

    companion object {
        const val VIEW_TYPE_ACCOUNT = 0
        const val VIEW_TYPE_FOOTER = 1
    }
}
