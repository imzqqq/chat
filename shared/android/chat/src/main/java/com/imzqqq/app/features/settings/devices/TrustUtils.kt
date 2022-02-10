package com.imzqqq.app.features.settings.devices

import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import org.matrix.android.sdk.internal.crypto.crosssigning.DeviceTrustLevel

object TrustUtils {

    fun shieldForTrust(currentDevice: Boolean,
                       trustMSK: Boolean,
                       legacyMode: Boolean,
                       deviceTrustLevel: DeviceTrustLevel?): RoomEncryptionTrustLevel {
        return when {
            currentDevice -> {
                if (legacyMode) {
                    // In legacy, current session is always trusted
                    RoomEncryptionTrustLevel.Trusted
                } else {
                    // If current session doesn't trust MSK, show red shield for current device
                    if (trustMSK) {
                        RoomEncryptionTrustLevel.Trusted
                    } else {
                        RoomEncryptionTrustLevel.Warning
                    }
                }
            }
            else          -> {
                if (legacyMode) {
                    // use local trust
                    if (deviceTrustLevel?.locallyVerified == true) {
                        RoomEncryptionTrustLevel.Trusted
                    } else {
                        RoomEncryptionTrustLevel.Warning
                    }
                } else {
                    if (trustMSK) {
                        // use cross sign trust, put locally trusted in black
                        when {
                            deviceTrustLevel?.crossSigningVerified == true -> RoomEncryptionTrustLevel.Trusted

                            deviceTrustLevel?.locallyVerified == true      -> RoomEncryptionTrustLevel.Default
                            else                                           -> RoomEncryptionTrustLevel.Warning
                        }
                    } else {
                        // The current session is untrusted, so displays others in black
                        // as we can't know the cross-signing state
                        RoomEncryptionTrustLevel.Default
                    }
                }
            }
        }
    }
}
