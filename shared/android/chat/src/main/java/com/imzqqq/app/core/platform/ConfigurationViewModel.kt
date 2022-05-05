package com.imzqqq.app.core.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imzqqq.app.core.extensions.postLiveEvent
import com.imzqqq.app.core.utils.LiveEvent
import com.imzqqq.app.features.configuration.VectorConfiguration
import timber.log.Timber
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
        private val vectorConfiguration: VectorConfiguration
) : ViewModel() {

    private var currentConfigurationValue: String? = null

    private val _activityRestarter = MutableLiveData<LiveEvent<Unit>>()
    val activityRestarter: LiveData<LiveEvent<Unit>>
        get() = _activityRestarter

    fun onActivityResumed() {
        if (currentConfigurationValue == null) {
            currentConfigurationValue = vectorConfiguration.getHash()
            Timber.v("Configuration: init to $currentConfigurationValue")
        } else {
            val newHash = vectorConfiguration.getHash()
            Timber.v("Configuration: newHash $newHash")

            if (newHash != currentConfigurationValue) {
                Timber.v("Configuration: recreate the Activity")
                currentConfigurationValue = newHash
                _activityRestarter.postLiveEvent(Unit)
            }
        }
    }
}
