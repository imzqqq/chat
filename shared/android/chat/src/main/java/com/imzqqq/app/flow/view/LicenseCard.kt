package com.imzqqq.app.flow.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.imzqqq.app.R
import com.imzqqq.app.databinding.CardLicenseBinding
import com.imzqqq.app.flow.util.LinkHelper
import com.imzqqq.app.flow.util.ThemeUtils
import com.imzqqq.app.flow.util.hide

class LicenseCard
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    init {
        val binding = CardLicenseBinding.inflate(LayoutInflater.from(context), this)

        setCardBackgroundColor(ThemeUtils.getColor(context, R.attr.colorSurface))

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LicenseCard, 0, 0)

        val name: String? = a.getString(R.styleable.LicenseCard_name)
        val license: String? = a.getString(R.styleable.LicenseCard_license)
        val link: String? = a.getString(R.styleable.LicenseCard_link)
        a.recycle()

        binding.licenseCardName.text = name
        binding.licenseCardLicense.text = license
        if (link.isNullOrBlank()) {
            binding.licenseCardLink.hide()
        } else {
            binding.licenseCardLink.text = link
            setOnClickListener { com.imzqqq.app.flow.util.LinkHelper.openLink(link, context) }
        }
    }
}
