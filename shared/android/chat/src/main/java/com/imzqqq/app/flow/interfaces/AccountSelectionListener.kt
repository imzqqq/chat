package com.imzqqq.app.flow.interfaces

import com.imzqqq.app.flow.db.AccountEntity

interface AccountSelectionListener {
    fun onAccountSelected(account: AccountEntity)
}
