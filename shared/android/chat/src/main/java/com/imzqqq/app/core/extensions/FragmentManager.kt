package com.imzqqq.app.core.extensions

import androidx.fragment.app.FragmentTransaction
import org.matrix.android.sdk.api.extensions.tryOrNull

inline fun androidx.fragment.app.FragmentManager.commitTransactionNow(func: FragmentTransaction.() -> FragmentTransaction) {
    // Could throw and make the app crash
    // e.g sharedActionViewModel.observe()
    tryOrNull("Failed to commitTransactionNow") {
        beginTransaction().func().commitNow()
    }
}

inline fun androidx.fragment.app.FragmentManager.commitTransaction(allowStateLoss: Boolean = false, func: FragmentTransaction.() -> FragmentTransaction) {
    val transaction = beginTransaction().func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}
