package com.imzqqq.app.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object DefaultSharedPreferences {

    @Volatile private var INSTANCE: SharedPreferences? = null

    fun getInstance(context: Context): SharedPreferences =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceManager.getDefaultSharedPreferences(context.applicationContext).also { INSTANCE = it }
            }
}
