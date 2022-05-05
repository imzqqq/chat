package com.imzqqq.app.features.roommemberprofile.devices

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for device list screen
 */
sealed class DeviceListBottomSheetViewEvents : VectorViewEvents {
    data class Verify(val userId: String, val txID: String) : DeviceListBottomSheetViewEvents()
}
