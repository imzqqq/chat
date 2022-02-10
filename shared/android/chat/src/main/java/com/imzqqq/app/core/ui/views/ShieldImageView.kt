package com.imzqqq.app.core.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.imzqqq.app.R
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel

class ShieldImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        if (isInEditMode) {
            render(RoomEncryptionTrustLevel.Trusted)
        }
    }

    fun render(roomEncryptionTrustLevel: RoomEncryptionTrustLevel?) {
        isVisible = roomEncryptionTrustLevel != null

        when (roomEncryptionTrustLevel) {
            RoomEncryptionTrustLevel.Default -> {
                contentDescription = context.getString(R.string.a11y_trust_level_default)
                setImageResource(R.drawable.ic_shield_black)
            }
            RoomEncryptionTrustLevel.Warning -> {
                contentDescription = context.getString(R.string.a11y_trust_level_warning)
                setImageResource(R.drawable.ic_shield_warning)
            }
            RoomEncryptionTrustLevel.Trusted -> {
                contentDescription = context.getString(R.string.a11y_trust_level_trusted)
                setImageResource(R.drawable.ic_shield_trusted)
            }
        }
    }
}

@DrawableRes
fun RoomEncryptionTrustLevel.toDrawableRes(): Int {
    return when (this) {
        RoomEncryptionTrustLevel.Default -> R.drawable.ic_shield_black
        RoomEncryptionTrustLevel.Warning -> R.drawable.ic_shield_warning
        RoomEncryptionTrustLevel.Trusted -> R.drawable.ic_shield_trusted
    }
}
