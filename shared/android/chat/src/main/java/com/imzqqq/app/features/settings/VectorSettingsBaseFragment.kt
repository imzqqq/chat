package com.imzqqq.app.features.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.imzqqq.app.core.preference.ScPreferenceFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.extensions.singletonEntryPoint
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.core.utils.toast
import org.matrix.android.sdk.api.session.Session
import timber.log.Timber

abstract class VectorSettingsBaseFragment : ScPreferenceFragment() {

    val vectorActivity: VectorBaseActivity<*> by lazy {
        activity as VectorBaseActivity<*>
    }

    private var mLoadingView: View? = null

    // members
    protected lateinit var session: Session
    protected lateinit var errorFormatter: ErrorFormatter

    abstract val preferenceXmlRes: Int

    @CallSuper
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(preferenceXmlRes)
        bindPref()
    }

    override fun onAttach(context: Context) {
        val singletonEntryPoint = context.singletonEntryPoint()
        super.onAttach(context)
        session = singletonEntryPoint.activeSessionHolder().getActiveSession()
        errorFormatter = singletonEntryPoint.errorFormatter()
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Fragment ${javaClass.simpleName}")
        vectorActivity.supportActionBar?.setTitle(titleRes)
        // find the view from parent activity
        mLoadingView = vectorActivity.findViewById(R.id.vector_settings_spinner_views)
    }

    abstract fun bindPref()

    abstract var titleRes: Int

    /* ==========================================================================================
     * Protected
     * ========================================================================================== */

    protected fun notImplemented() {
        // Snackbar cannot be display on PreferenceFragment. TODO It's maybe because the show() method is not used...
        // Snackbar.make(requireView(), R.string.not_implemented, Snackbar.LENGTH_SHORT)
        activity?.toast(R.string.not_implemented)
    }

    /**
     * Display the loading view.
     */
    protected fun displayLoadingView() {
        // search the loading view from the upper view
        if (null == mLoadingView) {
            var parent = view

            while (parent != null && mLoadingView == null) {
                mLoadingView = parent.findViewById(R.id.vector_settings_spinner_views)
                parent = parent.parent as View
            }
        } else {
            mLoadingView?.visibility = View.VISIBLE
        }
    }

    /**
     * Hide the loading view.
     */
    protected fun hideLoadingView() {
        mLoadingView?.visibility = View.GONE
    }

    /**
     * Hide the loading view and refresh the preferences.
     *
     * @param refresh true to refresh the display
     */
    protected fun hideLoadingView(refresh: Boolean) {
        mLoadingView?.visibility = View.GONE

        if (refresh) {
            // TODO refreshDisplay()
        }
    }

    /**
     * A request has been processed.
     * Display a toast if there is a an error message
     *
     * @param errorMessage the error message
     */
    protected fun onCommonDone(errorMessage: String?) {
        if (!isAdded) {
            return
        }
        activity?.runOnUiThread {
            if (errorMessage != null && errorMessage.isNotBlank()) {
                displayErrorDialog(errorMessage)
            }
            hideLoadingView()
        }
    }

    protected fun displayErrorDialog(throwable: Throwable) {
        displayErrorDialog(errorFormatter.toHumanReadable(throwable))
    }

    protected fun displayErrorDialog(errorMessage: String) {
        MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.dialog_title_error)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.ok, null)
                .show()
    }
}
