package com.imzqqq.app.core.platform.livedata

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

abstract class SharedPreferenceLiveData<T>(
    protected val sharedPrefs: SharedPreferences,
    protected val key: String,
    private val defValue: T
) : LiveData<T>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) {
            value = getValueFromPreferences(key, defValue)
        }
    }

    abstract fun getValueFromPreferences(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }

    companion object {
        fun booleanLiveData(
            sharedPrefs: SharedPreferences, key: String, defaultValue: Boolean
        ): SharedPreferenceLiveData<Boolean> {
            return object : SharedPreferenceLiveData<Boolean>(
                sharedPrefs, key, defaultValue
            ) {
                override fun getValueFromPreferences(key: String, defValue: Boolean): Boolean {
                    return this.sharedPrefs.getBoolean(key, defValue)
                }
            }
        }
    }
}
