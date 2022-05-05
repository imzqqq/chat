package com.imzqqq.app.flow.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.Spanned
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.json.SpannedTypeAdapter
import com.imzqqq.app.flow.network.InstanceSwitchAuthInterceptor
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.getNonNullString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesGson(): Gson = GsonBuilder().registerTypeAdapter(Spanned::class.java, SpannedTypeAdapter()).create()

    @Provides
    @Singleton
    @JvmStatic
    fun providesHttpClient(
            accountManager: AccountManager,
            context: Context,
            preferences: SharedPreferences
    ): OkHttpClient {
        val httpProxyEnabled = preferences.getBoolean("httpProxyEnabled"     , false)
        val httpServer       = preferences.getNonNullString("httpProxyServer", "")
        val httpPort         = preferences.getNonNullString("httpProxyPort"  , "-1").toIntOrNull() ?: -1
        val cacheSize        = 25 * 1024 * 1024L                                                         // 25 MiB
        val builder          = OkHttpClient.Builder()
            .addInterceptor { chain ->
                /**
                 * Add a custom User-Agent that contains Flow, Android and OkHttp Version to all requests
                 * Example:
                 * User-Agent: Flow/1.1.2 Android/5.0.2 OkHttp/4.9.0
                 * */
                val requestWithUserAgent = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "Flow/${BuildConfig.VERSION_NAME} Android/${Build.VERSION.RELEASE} OkHttp/${OkHttp.VERSION}"
                    )
                    .build()
                chain.proceed(requestWithUserAgent)
            }
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, cacheSize))

        if (httpProxyEnabled && httpServer.isNotEmpty() && httpPort > 0 && httpPort < 65535) {
            val address = InetSocketAddress.createUnresolved(httpServer, httpPort)
            builder.proxy(Proxy(Proxy.Type.HTTP, address))
        }
        return builder
            .apply {
                addInterceptor(com.imzqqq.app.flow.network.InstanceSwitchAuthInterceptor(accountManager))
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                }
            }
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun providesRetrofit(
        httpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder().baseUrl("https://" + MastodonApi.PLACEHOLDER_DOMAIN)
                            .client(httpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun providesMastodonApi(retrofit: Retrofit): MastodonApi = retrofit.create()
}
