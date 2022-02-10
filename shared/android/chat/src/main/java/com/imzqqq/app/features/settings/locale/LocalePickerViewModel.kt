package com.imzqqq.app.features.settings.locale

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.configuration.VectorConfiguration
import com.imzqqq.app.features.settings.VectorLocale
import kotlinx.coroutines.launch

class LocalePickerViewModel @AssistedInject constructor(
        @Assisted initialState: LocalePickerViewState,
        private val vectorConfiguration: VectorConfiguration
) : VectorViewModel<LocalePickerViewState, LocalePickerAction, LocalePickerViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<LocalePickerViewModel, LocalePickerViewState> {
        override fun create(initialState: LocalePickerViewState): LocalePickerViewModel
    }

    init {
        viewModelScope.launch {
            val result = VectorLocale.getSupportedLocales()

            setState {
                copy(
                        locales = Success(result)
                )
            }
        }
    }

    companion object : MavericksViewModelFactory<LocalePickerViewModel, LocalePickerViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: LocalePickerAction) {
        when (action) {
            is LocalePickerAction.SelectLocale -> handleSelectLocale(action)
        }.exhaustive
    }

    private fun handleSelectLocale(action: LocalePickerAction.SelectLocale) {
        VectorLocale.saveApplicationLocale(action.locale)
        vectorConfiguration.applyToApplicationContext()
        _viewEvents.post(LocalePickerViewEvents.RestartActivity)
    }
}
