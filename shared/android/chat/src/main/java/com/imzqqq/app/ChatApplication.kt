package com.imzqqq.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Handler
import android.os.HandlerThread
import android.os.StrictMode
import android.util.Log
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.emoji.text.EmojiCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.preference.PreferenceManager
import autodispose2.AutoDisposePlugins
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Mavericks
import com.facebook.stetho.Stetho
import com.gabrielittner.threetenbp.LazyThreeTen
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp
import de.spiritcroc.matrixsdk.StaticScSdkHelper
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.extensions.configureAndStart
import com.imzqqq.app.core.extensions.startSyncing
import com.imzqqq.app.features.call.webrtc.WebRtcCallManager
import com.imzqqq.app.features.configuration.VectorConfiguration
import com.imzqqq.app.features.disclaimer.doNotShowDisclaimerDialog
import com.imzqqq.app.features.invite.InvitesAcceptor
import com.imzqqq.app.features.lifecycle.VectorActivityLifecycleCallbacks
import com.imzqqq.app.features.notifications.NotificationDrawerManager
import com.imzqqq.app.features.notifications.NotificationUtils
import com.imzqqq.app.features.pin.PinLocker
import com.imzqqq.app.features.popup.PopupAlertManager
import com.imzqqq.app.features.rageshake.VectorFileLogger
import com.imzqqq.app.features.rageshake.VectorUncaughtExceptionHandler
import com.imzqqq.app.features.room.VectorRoomDisplayNameFallbackProvider
import com.imzqqq.app.features.settings.VectorLocale
import com.imzqqq.app.features.settings.VectorPreferences
import com.imzqqq.app.features.themes.ThemeUtils
import com.imzqqq.app.features.version.VersionProvider
import com.imzqqq.app.core.pushers.StateHelper
import com.imzqqq.app.flow.components.notifications.NotificationWorkerFactory
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.EmojiCompatFont
import com.imzqqq.app.flow.util.LocaleManager
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.conscrypt.Conscrypt
import org.jitsi.meet.sdk.log.JitsiMeetDefaultLogHandler
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.MatrixConfiguration
import org.matrix.android.sdk.api.auth.AuthenticationService
import org.matrix.android.sdk.api.legacy.LegacySessionImporter
import timber.log.Timber
import java.security.Security
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import javax.inject.Inject
import androidx.work.Configuration as WorkConfiguration

@HiltAndroidApp
class ChatApplication :
        Application(),
        MatrixConfiguration.Provider,
        WorkConfiguration.Provider {

    lateinit var appContext: Context
    @Inject lateinit var legacySessionImporter: LegacySessionImporter
    @Inject lateinit var authenticationService: AuthenticationService
    @Inject lateinit var vectorConfiguration: VectorConfiguration
    @Inject lateinit var emojiCompatFontProvider: EmojiCompatFontProvider
    @Inject lateinit var emojiCompatWrapper: EmojiCompatWrapper
    @Inject lateinit var vectorUncaughtExceptionHandler: VectorUncaughtExceptionHandler
    @Inject lateinit var activeSessionHolder: ActiveSessionHolder
    @Inject lateinit var notificationDrawerManager: NotificationDrawerManager
    @Inject lateinit var vectorPreferences: VectorPreferences
    @Inject lateinit var versionProvider: VersionProvider
    @Inject lateinit var notificationUtils: NotificationUtils
    @Inject lateinit var appStateHandler: AppStateHandler
    @Inject lateinit var popupAlertManager: PopupAlertManager
    @Inject lateinit var pinLocker: PinLocker
    @Inject lateinit var callManager: WebRtcCallManager
    @Inject lateinit var invitesAcceptor: InvitesAcceptor
    @Inject lateinit var vectorFileLogger: VectorFileLogger
    /// MARK - imzqqq
    @Inject lateinit var notificationWorkerFactory: NotificationWorkerFactory

    //companion object {
    //    @JvmStatic
    //    lateinit var localeManager: LocaleManager
    //}
    /// END

    // font thread handler
    private var fontThreadHandler: Handler? = null

    private val powerKeyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF &&
                    vectorPreferences.useFlagPinCode()) {
                pinLocker.screenIsOff()
            }
        }
    }

    override fun onCreate() {
        enableStrictModeIfNeeded()
        super.onCreate()
        appContext = this
        invitesAcceptor.initialize()
        vectorUncaughtExceptionHandler.activate(this)

        // SC SDK helper initialization
        StaticScSdkHelper.scSdkPreferenceProvider = vectorPreferences

        // Remove Log handler statically added by Jitsi
        Timber.forest()
                .filterIsInstance(JitsiMeetDefaultLogHandler::class.java)
                .forEach { Timber.uproot(it) }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(vectorFileLogger)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        logInfo()
        LazyThreeTen.init(this)
        Mavericks.initialize(debugMode = false)
        EpoxyController.defaultDiffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        EpoxyController.defaultModelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        registerActivityLifecycleCallbacks(VectorActivityLifecycleCallbacks(popupAlertManager))
        val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
        )
        FontsContractCompat.requestFont(this, fontRequest, emojiCompatFontProvider, getFontThreadHandler())
        VectorLocale.init(this)
        ThemeUtils.init(this)
        vectorConfiguration.applyToApplicationContext()

        emojiCompatWrapper.init(fontRequest)

        notificationUtils.createNotificationChannels()

        // It can takes time, but do we care?
        val sessionImported = legacySessionImporter.process()
        if (!sessionImported) {
            // Do not display the name change popup
            doNotShowDisclaimerDialog(this)
        }

        if (authenticationService.hasAuthenticatedSessions() && !activeSessionHolder.hasActiveSession()) {
            val lastAuthenticatedSession = authenticationService.getLastAuthenticatedSession()!!
            activeSessionHolder.setActiveSession(lastAuthenticatedSession)
            lastAuthenticatedSession.configureAndStart(applicationContext, startSyncing = false)
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(startSyncOnFirstStart)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                Timber.i("App entered foreground")
                StateHelper.onEnterForeground(appContext, activeSessionHolder)
                activeSessionHolder.getSafeActiveSession()?.also {
                    it.stopAnyBackgroundSync()
                }
            }

            override fun onPause(owner: LifecycleOwner) {
                Timber.i("App entered background") // call persistInfo
                notificationDrawerManager.persistInfo()
                StateHelper.onEnterBackground(appContext, vectorPreferences, activeSessionHolder)
            }
        })
        ProcessLifecycleOwner.get().lifecycle.addObserver(appStateHandler)
        ProcessLifecycleOwner.get().lifecycle.addObserver(pinLocker)
        ProcessLifecycleOwner.get().lifecycle.addObserver(callManager)
        // This should be done as early as possible
        // initKnownEmojiHashSet(appContext)

        applicationContext.registerReceiver(powerKeyReceiver, IntentFilter().apply {
            // Looks like i cannot receive OFF, if i don't have both ON and OFF
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        })

        EmojiManager.install(GoogleEmojiProvider())

        /// MARK - imzqqq
        //Security.insertProviderAt(Conscrypt.newProvider(), 1)
        //AutoDisposePlugins.setHideProxies(false) // a small performance optimization

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        // init the custom emoji fonts
        val emojiSelection = preferences.getInt(PrefKeys.EMOJI, 0)
        val emojiConfig = EmojiCompatFont.byId(emojiSelection)
                .getConfig(this)
                .setReplaceAll(true)
        EmojiCompat.init(emojiConfig)

        // init night mode
        //val theme = preferences.getString("appTheme", com.imzqqq.app.flow.util.ThemeUtils.APP_THEME_DEFAULT)
        //com.imzqqq.app.flow.util.ThemeUtils.setAppNightMode(theme)

        //RxJavaPlugins.setErrorHandler {
        //    Timber.tag("RxJava").w(it, "undeliverable exception")
        //}
		///
    }

    private val startSyncOnFirstStart = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            Timber.i("App process started")
            authenticationService.getLastAuthenticatedSession()?.startSyncing(appContext)
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        }
    }

    private fun enableStrictModeIfNeeded() {
        if (BuildConfig.ENABLE_STRICT_MODE_LOGS) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }

    override fun providesMatrixConfiguration(): MatrixConfiguration {
        return MatrixConfiguration(
                applicationFlavor = BuildConfig.FLAVOR_DESCRIPTION,
                roomDisplayNameFallbackProvider = VectorRoomDisplayNameFallbackProvider(this)
        )
    }

    override fun getWorkManagerConfiguration(): WorkConfiguration {
        return WorkConfiguration.Builder()
                .setExecutor(Executors.newCachedThreadPool())
                /// MARK
                .setWorkerFactory(notificationWorkerFactory)
                ///
                .build()
    }

    private fun logInfo() {
        val appVersion = versionProvider.getVersion(longFormat = true, useBuildNumber = true)
        val sdkVersion = Matrix.getSdkVersion()
        val date = SimpleDateFormat("MM-dd HH:mm:ss.SSSZ", Locale.US).format(Date())

        Timber.v("----------------------------------------------------------------")
        Timber.v("----------------------------------------------------------------")
        Timber.v(" Application version: $appVersion")
        Timber.v(" SDK version: $sdkVersion")
        Timber.v(" Local time: $date")
        Timber.v("----------------------------------------------------------------")
        Timber.v("----------------------------------------------------------------\n\n\n\n")
    }

    override fun attachBaseContext(base: Context) {
        /// MARK
        //localeManager = LocaleManager(base)
        super.attachBaseContext(base)
        //super.attachBaseContext(localeManager.setLocale(base))
        ///
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        vectorConfiguration.onConfigurationChanged()
        /// MARK
        //localeManager.setLocale(this)
        ///
    }

    private fun getFontThreadHandler(): Handler {
        return fontThreadHandler ?: createFontThreadHandler().also {
            fontThreadHandler = it
        }
    }

    private fun createFontThreadHandler(): Handler {
        val handlerThread = HandlerThread("fonts")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }
}
