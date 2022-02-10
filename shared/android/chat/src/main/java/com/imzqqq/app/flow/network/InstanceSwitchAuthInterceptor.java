package com.imzqqq.app.flow.network;

import androidx.annotation.NonNull;

import com.imzqqq.app.flow.db.AccountEntity;
import com.imzqqq.app.flow.db.AccountManager;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class InstanceSwitchAuthInterceptor implements Interceptor {

    private final AccountManager accountManager;

    public InstanceSwitchAuthInterceptor(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originalRequest = chain.request();

        // only switch domains if the request comes from retrofit
        if (originalRequest.url().host().equals(MastodonApi.PLACEHOLDER_DOMAIN)) {
            AccountEntity currentAccount = accountManager.getActiveAccount();

            Request.Builder builder = originalRequest.newBuilder();

            String instanceHeader = originalRequest.header(MastodonApi.DOMAIN_HEADER);
            if (instanceHeader != null) {
                // use domain explicitly specified in custom header
                builder.url(swapHost(originalRequest.url(), instanceHeader));
                builder.removeHeader(MastodonApi.DOMAIN_HEADER);
            } else if (currentAccount != null) {
                //use domain of current account
                builder.url(swapHost(originalRequest.url(), currentAccount.getDomain()))
                        .header("Authorization",
                                String.format("Bearer %s", currentAccount.getAccessToken()));
            }
            Request newRequest = builder.build();

            return chain.proceed(newRequest);

        } else {
            return chain.proceed(originalRequest);
        }
    }

    @NonNull
    private static HttpUrl swapHost(@NonNull HttpUrl url, @NonNull String host) {
        return url.newBuilder().host(host).build();
    }
}
