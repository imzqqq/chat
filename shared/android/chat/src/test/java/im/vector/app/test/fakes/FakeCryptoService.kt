package com.imzqqq.app.test.fakes

import androidx.lifecycle.MutableLiveData
import io.mockk.mockk
import org.matrix.android.sdk.api.session.crypto.CryptoService
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo

class FakeCryptoService : CryptoService by mockk() {

    var roomKeysExport = ByteArray(size = 1)
    var cryptoDeviceInfos = mutableMapOf<String, CryptoDeviceInfo>()

    override suspend fun exportRoomKeys(password: String) = roomKeysExport

    override fun getLiveCryptoDeviceInfo() = MutableLiveData(cryptoDeviceInfos.values.toList())

    override fun getLiveCryptoDeviceInfo(userId: String) = getLiveCryptoDeviceInfo(listOf(userId))

    override fun getLiveCryptoDeviceInfo(userIds: List<String>) = MutableLiveData(
            cryptoDeviceInfos.filterKeys { userIds.contains(it) }.values.toList()
    )
}
