package com.imzqqq.app.flow.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.imzqqq.app.flow.ViewMediaActivity
import com.imzqqq.app.flow.entity.Attachment

abstract class ViewMediaFragment : Fragment() {
    private var toolbarVisibiltyDisposable: Function0<Boolean>? = null

    abstract fun setupMediaView(
        url: String,
        previewUrl: String?,
        description: String?,
        showingDescription: Boolean
    )

    abstract fun onToolbarVisibilityChange(visible: Boolean)

    protected var showingDescription = false
    protected var isDescriptionVisible = false

    companion object {
        @JvmStatic
        protected val ARG_START_POSTPONED_TRANSITION = "startPostponedTransition"

        @JvmStatic
        protected val ARG_ATTACHMENT = "attach"
        @JvmStatic
        protected val ARG_SINGLE_IMAGE_URL = "singleImageUrl"

        @JvmStatic
        fun newInstance(attachment: Attachment, shouldStartPostponedTransition: Boolean): ViewMediaFragment {
            val arguments = Bundle(2)
            arguments.putParcelable(ARG_ATTACHMENT, attachment)
            arguments.putBoolean(ARG_START_POSTPONED_TRANSITION, shouldStartPostponedTransition)

            val fragment = when (attachment.type) {
                Attachment.Type.IMAGE -> ViewImageFragment()
                Attachment.Type.VIDEO,
                Attachment.Type.GIFV,
                Attachment.Type.AUDIO -> ViewVideoFragment()
                else                  -> ViewImageFragment() // it probably won't show anything, but its better than crashing
            }
            fragment.arguments = arguments
            return fragment
        }

        @JvmStatic
        fun newSingleImageInstance(imageUrl: String): ViewMediaFragment {
            val arguments = Bundle(2)
            val fragment = ViewImageFragment()
            arguments.putString(ARG_SINGLE_IMAGE_URL, imageUrl)
            arguments.putBoolean(ARG_START_POSTPONED_TRANSITION, true)

            fragment.arguments = arguments
            return fragment
        }
    }

    abstract fun onTransitionEnd()

    protected fun finalizeViewSetup(url: String, previewUrl: String?, description: String?) {
        val mediaActivity = activity as ViewMediaActivity

        showingDescription = !TextUtils.isEmpty(description)
        isDescriptionVisible = showingDescription
        setupMediaView(url, previewUrl, description, showingDescription && mediaActivity.isToolbarVisible)

        toolbarVisibiltyDisposable = (activity as ViewMediaActivity)
            .addToolbarVisibilityListener { isVisible ->
                onToolbarVisibilityChange(isVisible)
            }
    }

    override fun onDestroyView() {
        toolbarVisibiltyDisposable?.invoke()
        super.onDestroyView()
    }
}
