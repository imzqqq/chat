package com.imzqqq.app.core.extensions

import android.os.Bundle
import android.os.Parcelable
import com.airbnb.mvrx.Mavericks

fun Parcelable?.toMvRxBundle(): Bundle? {
    return this?.let { Bundle().apply { putParcelable(Mavericks.KEY_ARG, it) } }
}
