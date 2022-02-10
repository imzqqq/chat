package com.imzqqq.app.features.login

import com.imzqqq.app.core.utils.TemporaryStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Will store the account password for 3 minutes
 */
@Singleton
class ReAuthHelper @Inject constructor() : TemporaryStore<String>()
