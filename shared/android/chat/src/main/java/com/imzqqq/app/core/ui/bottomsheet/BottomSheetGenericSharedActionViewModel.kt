package com.imzqqq.app.core.ui.bottomsheet

import com.imzqqq.app.core.platform.VectorSharedAction
import com.imzqqq.app.core.platform.VectorSharedActionViewModel

/**
 * Activity shared view model to handle bottom sheet quick actions
 */
abstract class BottomSheetGenericSharedActionViewModel<Action : VectorSharedAction> : VectorSharedActionViewModel<Action>()
