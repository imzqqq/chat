package com.imzqqq.app.features.debug.sas

import com.airbnb.epoxy.TypedEpoxyController
import org.matrix.android.sdk.api.session.crypto.verification.EmojiRepresentation

data class SasState(
        val emojiList: List<EmojiRepresentation>
)

class SasEmojiController : TypedEpoxyController<SasState>() {

    override fun buildModels(data: SasState?) {
        if (data == null) return

        data.emojiList.forEachIndexed { idx, emojiRepresentation ->
            sasEmojiItem {
                id(idx)
                index(idx)
                emojiRepresentation(emojiRepresentation)
            }
        }
    }
}
