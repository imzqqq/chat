package com.imzqqq.app.core.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.imzqqq.app.core.dispatchers.CoroutineDispatchers
import com.imzqqq.app.core.error.DefaultErrorFormatter
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.features.invite.AutoAcceptInvites
import com.imzqqq.app.features.invite.CompileTimeAutoAcceptInvites
import com.imzqqq.app.features.navigation.DefaultNavigator
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.pin.PinCodeStore
import com.imzqqq.app.features.pin.SharedPrefPinCodeStore
import com.imzqqq.app.features.ui.SharedPreferencesUiStateRepository
import com.imzqqq.app.features.ui.UiStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.auth.AuthenticationService
import org.matrix.android.sdk.api.auth.HomeServerHistoryService
import org.matrix.android.sdk.api.legacy.LegacySessionImporter
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class VectorBindModule {

    @Binds
    abstract fun bindNavigator(navigator: DefaultNavigator): Navigator

    @Binds
    abstract fun bindErrorFormatter(formatter: DefaultErrorFormatter): ErrorFormatter

    @Binds
    abstract fun bindUiStateRepository(repository: SharedPreferencesUiStateRepository): UiStateRepository

    @Binds
    abstract fun bindPinCodeStore(store: SharedPrefPinCodeStore): PinCodeStore

    @Binds
    abstract fun bindAutoAcceptInvites(autoAcceptInvites: CompileTimeAutoAcceptInvites): AutoAcceptInvites
}

@InstallIn(SingletonComponent::class)
@Module
object VectorStaticModule {

    @Provides
    @JvmStatic
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @JvmStatic
    fun providesResources(context: Context): Resources {
        return context.resources
    }

    @Provides
    @JvmStatic
    fun providesSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("im.vector.riot", MODE_PRIVATE)
    }

    @Provides
    @JvmStatic
    fun providesMatrix(context: Context): Matrix {
        return Matrix.getInstance(context)
    }

    @Provides
    @JvmStatic
    fun providesCurrentSession(activeSessionHolder: ActiveSessionHolder): Session {
        // TODO: handle session injection better
        return activeSessionHolder.getActiveSession()
    }

    @Provides
    @JvmStatic
    fun providesLegacySessionImporter(matrix: Matrix): LegacySessionImporter {
        return matrix.legacySessionImporter()
    }

    @Provides
    @JvmStatic
    fun providesAuthenticationService(matrix: Matrix): AuthenticationService {
        return matrix.authenticationService()
    }

    @Provides
    @JvmStatic
    fun providesRawService(matrix: Matrix): RawService {
        return matrix.rawService()
    }

    @Provides
    @JvmStatic
    fun providesHomeServerHistoryService(matrix: Matrix): HomeServerHistoryService {
        return matrix.homeServerHistoryService()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun providesApplicationCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    @Provides
    @JvmStatic
    fun providesCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchers(io = Dispatchers.IO, computation = Dispatchers.Default)
    }
}
