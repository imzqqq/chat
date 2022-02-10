package com.imzqqq.app.features.settings.devtools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.utils.createJSonViewerStyleProvider
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import org.billcarsonfr.jsonviewer.JSonViewerDialog
import org.matrix.android.sdk.api.session.accountdata.UserAccountDataEvent
import org.matrix.android.sdk.internal.di.MoshiProvider
import javax.inject.Inject

class AccountDataFragment @Inject constructor(
        private val epoxyController: AccountDataEpoxyController,
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentGenericRecyclerBinding>(),
        AccountDataEpoxyController.InteractionListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: AccountDataViewModel by fragmentViewModel(AccountDataViewModel::class)

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.settings_account_data)
    }

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(epoxyController, dividerDrawable = R.drawable.divider_horizontal)
        epoxyController.interactionListener = this
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        epoxyController.interactionListener = null
        super.onDestroyView()
    }

    override fun didTap(data: UserAccountDataEvent) {
        val jsonString = MoshiProvider.providesMoshi()
                .adapter(UserAccountDataEvent::class.java)
                .toJson(data)
        JSonViewerDialog.newInstance(
                jsonString,
                -1, // open All
                createJSonViewerStyleProvider(colorProvider)
        ).show(childFragmentManager, "JSON_VIEWER")
    }

    override fun didLongTap(data: UserAccountDataEvent) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_Vector_MaterialAlertDialog_Destructive)
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.delete_account_data_warning, data.type))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewModel.handle(AccountDataAction.DeleteAccountData(data.type))
                }
                .show()
    }
}
