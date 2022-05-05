package com.imzqqq.app.core.extensions

import android.content.Context
import dagger.hilt.EntryPoints
import com.imzqqq.app.core.di.SingletonEntryPoint

fun Context.singletonEntryPoint(): SingletonEntryPoint {
    return EntryPoints.get(applicationContext, SingletonEntryPoint::class.java)
}
