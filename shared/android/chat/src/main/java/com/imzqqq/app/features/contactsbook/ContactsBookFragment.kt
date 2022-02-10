package com.imzqqq.app.features.contactsbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.showIdentityServerConsentDialog
import com.imzqqq.app.databinding.FragmentContactsBookBinding
import com.imzqqq.app.features.navigation.SettingsActivityPayload
import com.imzqqq.app.features.userdirectory.PendingSelection
import com.imzqqq.app.features.userdirectory.UserListAction
import com.imzqqq.app.features.userdirectory.UserListSharedAction
import com.imzqqq.app.features.userdirectory.UserListSharedActionViewModel
import com.imzqqq.app.features.userdirectory.UserListViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.session.identity.ThreePid
import org.matrix.android.sdk.api.session.user.model.User
import reactivecircus.flowbinding.android.widget.checkedChanges
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class ContactsBookFragment @Inject constructor(
        private val contactsBookController: ContactsBookController
) : VectorBaseFragment<FragmentContactsBookBinding>(), ContactsBookController.Callback {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentContactsBookBinding {
        return FragmentContactsBookBinding.inflate(inflater, container, false)
    }

    private val viewModel: UserListViewModel by activityViewModel()

    // Use activityViewModel to avoid loading several times the data
    private val contactsBookViewModel: ContactsBookViewModel by activityViewModel()

    private lateinit var sharedActionViewModel: UserListSharedActionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedActionViewModel = activityViewModelProvider.get(UserListSharedActionViewModel::class.java)
        setupRecyclerView()
        setupFilterView()
        setupConsentView()
        setupOnlyBoundContactsView()
        setupCloseView()
    }

    private fun setupConsentView() {
        views.phoneBookSearchForMatrixContacts.setOnClickListener {
            withState(contactsBookViewModel) { state ->
                requireContext().showIdentityServerConsentDialog(
                        state.identityServerUrl,
                        policyLinkCallback = {
                            navigator.openSettings(requireContext(), SettingsActivityPayload.DiscoverySettings(expandIdentityPolicies = true))
                        },
                        consentCallBack = { contactsBookViewModel.handle(ContactsBookAction.UserConsentGranted) }
                )
            }
        }
    }

    private fun setupOnlyBoundContactsView() {
        views.phoneBookOnlyBoundContacts.checkedChanges()
                .onEach {
                    contactsBookViewModel.handle(ContactsBookAction.OnlyBoundContacts(it))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupFilterView() {
        views.phoneBookFilter
                .textChanges()
                .skipInitialValue()
                .debounce(300)
                .onEach {
                    contactsBookViewModel.handle(ContactsBookAction.FilterWith(it.toString()))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        views.phoneBookRecyclerView.cleanup()
        contactsBookController.callback = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        contactsBookController.callback = this
        views.phoneBookRecyclerView.configureWith(contactsBookController)
    }

    private fun setupCloseView() {
        views.phoneBookClose.debouncedClicks {
            sharedActionViewModel.post(UserListSharedAction.GoBack)
        }
    }

    override fun invalidate() = withState(contactsBookViewModel) { state ->
        views.phoneBookSearchForMatrixContacts.isVisible = state.filteredMappedContacts.isNotEmpty() && state.identityServerUrl != null && !state.userConsent
        views.phoneBookOnlyBoundContacts.isVisible = state.isBoundRetrieved
        contactsBookController.setData(state)
    }

    override fun onMatrixIdClick(matrixId: String) {
        view?.hideKeyboard()
        viewModel.handle(UserListAction.AddPendingSelection(PendingSelection.UserPendingSelection(User(matrixId))))
        sharedActionViewModel.post(UserListSharedAction.GoBack)
    }

    override fun onThreePidClick(threePid: ThreePid) {
        view?.hideKeyboard()
        viewModel.handle(UserListAction.AddPendingSelection(PendingSelection.ThreePidPendingSelection(threePid)))
        sharedActionViewModel.post(UserListSharedAction.GoBack)
    }
}
