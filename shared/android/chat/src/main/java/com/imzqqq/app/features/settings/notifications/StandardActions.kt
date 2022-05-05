package com.imzqqq.app.features.settings.notifications

import org.matrix.android.sdk.api.pushrules.Action

sealed class StandardActions(
        val actions: List<Action>?
) {
    object Notify : StandardActions(actions = listOf(Action.Notify))
    object NotifyDefaultSound : StandardActions(actions = listOf(Action.Notify, Action.Sound()))
    object NotifyRingSound : StandardActions(actions = listOf(Action.Notify, Action.Sound(sound = Action.ACTION_OBJECT_VALUE_VALUE_RING)))
    object Highlight : StandardActions(actions = listOf(Action.Notify, Action.Highlight(highlight = true)))
    object HighlightDefaultSound : StandardActions(actions = listOf(Action.Notify, Action.Highlight(highlight = true), Action.Sound()))
    object DontNotify : StandardActions(actions = listOf(Action.DoNotNotify))
    object Disabled : StandardActions(actions = null)
}
