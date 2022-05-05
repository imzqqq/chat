package com.imzqqq.app.features.contactsbook

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class ContactsBookAction : VectorViewModelAction {
    data class FilterWith(val filter: String) : ContactsBookAction()
    data class OnlyBoundContacts(val onlyBoundContacts: Boolean) : ContactsBookAction()
    object UserConsentGranted : ContactsBookAction()
}
