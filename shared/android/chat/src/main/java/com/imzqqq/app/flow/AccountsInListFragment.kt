package com.imzqqq.app.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider.from
import autodispose2.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.FragmentAccountsInListBinding
import com.imzqqq.app.databinding.ItemFollowRequestBinding
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.*
import com.imzqqq.app.flow.viewmodel.AccountsInListViewModel
import com.imzqqq.app.flow.viewmodel.State
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.io.IOException
import javax.inject.Inject

private typealias AccountInfo = Pair<Account, Boolean>

@AndroidEntryPoint
class AccountsInListFragment @Inject constructor() : DialogFragment() {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: AccountsInListViewModel by viewModels { flowViewModelFactory }
    private lateinit var binding: FragmentAccountsInListBinding
    private lateinit var listId: String
    private lateinit var listName: String
    private val adapter = Adapter()
    private val searchAdapter = SearchAdapter()

    private val radius by lazy { resources.getDimensionPixelSize(R.dimen.avatar_radius_48dp) }
    private val pm by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }
    private val animateAvatar by lazy { pm.getBoolean(PrefKeys.ANIMATE_GIF_AVATARS, false) }
    private val animateEmojis by lazy { pm.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountsInListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FlowDialogFragmentStyle)
        val args = requireArguments()
        listId = args.getString(LIST_ID_ARG)!!
        listName = args.getString(LIST_NAME_ARG)!!

        viewModel.load(listId)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            // Stretch dialog to the window
            window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.accountsRecycler.layoutManager = LinearLayoutManager(view.context)
        binding.accountsRecycler.adapter = adapter

        binding.accountsSearchRecycler.layoutManager = LinearLayoutManager(view.context)
        binding.accountsSearchRecycler.adapter = searchAdapter

        viewModel.state
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(from(this))
            .subscribe { state ->
                adapter.submitList(state.accounts.asRightOrNull() ?: listOf())

                when (state.accounts) {
                    is Either.Right -> binding.messageView.hide()
                    is Either.Left  -> handleError(state.accounts.value)
                }

                setupSearchView(state)
            }

        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Close event is not sent so we use this instead
                if (newText.isNullOrBlank()) {
                    viewModel.search("")
                }
                return true
            }
        })
    }

    private fun setupSearchView(state: State) {
        if (state.searchResult == null) {
            searchAdapter.submitList(listOf())
            binding.accountsSearchRecycler.hide()
            binding.accountsRecycler.show()
        } else {
            val listAccounts = state.accounts.asRightOrNull() ?: listOf()
            val newList = state.searchResult.map { acc ->
                acc to listAccounts.contains(acc)
            }
            searchAdapter.submitList(newList)
            binding.accountsSearchRecycler.show()
            binding.accountsRecycler.hide()
        }
    }

    private fun handleError(error: Throwable) {
        binding.messageView.show()
        val retryAction = { _: View ->
            binding.messageView.hide()
            viewModel.load(listId)
        }
        if (error is IOException) {
            binding.messageView.setup(
                    R.drawable.elephant_offline,
                    R.string.error_network, retryAction
            )
        } else {
            binding.messageView.setup(
                    R.drawable.elephant_error,
                    R.string.error_generic, retryAction
            )
        }
    }

    private fun onRemoveFromList(accountId: String) {
        viewModel.deleteAccountFromList(listId, accountId)
    }

    private fun onAddToList(account: Account) {
        viewModel.addAccountToList(listId, account)
    }

    private object AccountDiffer : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.deepEquals(newItem)
        }
    }

    inner class Adapter : ListAdapter<Account, BindingHolder<ItemFollowRequestBinding>>(AccountDiffer) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemFollowRequestBinding> {
            val binding = ItemFollowRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = BindingHolder(binding)

            binding.notificationTextView.hide()
            binding.acceptButton.hide()
            binding.rejectButton.setOnClickListener {
                onRemoveFromList(getItem(holder.bindingAdapterPosition).id)
            }
            binding.rejectButton.contentDescription =
                binding.root.context.getString(R.string.action_remove_from_list)

            return holder
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemFollowRequestBinding>, position: Int) {
            val account = getItem(position)
            holder.binding.displayNameTextView.text = account.name.emojify(account.emojis, holder.binding.displayNameTextView, animateEmojis)
            holder.binding.usernameTextView.text = account.username
            loadAvatar(account.avatar, holder.binding.avatar, radius, animateAvatar)
        }
    }

    private object SearchDiffer : DiffUtil.ItemCallback<AccountInfo>() {
        override fun areItemsTheSame(oldItem: AccountInfo, newItem: AccountInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AccountInfo, newItem: AccountInfo): Boolean {
            return oldItem.second == newItem.second &&
                oldItem.first.deepEquals(newItem.first)
        }
    }

    inner class SearchAdapter : ListAdapter<AccountInfo, BindingHolder<ItemFollowRequestBinding>>(SearchDiffer) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemFollowRequestBinding> {
            val binding = ItemFollowRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = BindingHolder(binding)

            binding.notificationTextView.hide()
            binding.acceptButton.hide()
            binding.rejectButton.setOnClickListener {
                val (account, inAList) = getItem(holder.bindingAdapterPosition)
                if (inAList) {
                    onRemoveFromList(account.id)
                } else {
                    onAddToList(account)
                }
            }

            return holder
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemFollowRequestBinding>, position: Int) {
            val (account, inAList) = getItem(position)

            holder.binding.displayNameTextView.text = account.name.emojify(account.emojis, holder.binding.displayNameTextView, animateEmojis)
            holder.binding.usernameTextView.text = account.username
            loadAvatar(account.avatar, holder.binding.avatar, radius, animateAvatar)

            holder.binding.rejectButton.apply {
                contentDescription = if (inAList) {
                    setImageResource(R.drawable.ic_reject_24dp)
                    getString(R.string.action_remove_from_list)
                } else {
                    setImageResource(R.drawable.ic_plus_24dp)
                    getString(R.string.action_add_to_list)
                }
            }
        }
    }

    companion object {
        private const val LIST_ID_ARG = "listId"
        private const val LIST_NAME_ARG = "listName"

        @JvmStatic
        fun newInstance(listId: String, listName: String): AccountsInListFragment {
            val args = Bundle().apply {
                putString(LIST_ID_ARG, listId)
                putString(LIST_NAME_ARG, listName)
            }
            return AccountsInListFragment().apply { arguments = args }
        }
    }
}
